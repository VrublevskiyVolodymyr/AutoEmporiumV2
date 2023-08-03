package com.autoemporium.autoemporium.services;


import com.autoemporium.autoemporium.dao.AdvertisementViewDAO;
import com.autoemporium.autoemporium.dao.UserDAO;
import com.autoemporium.autoemporium.models.*;
import com.autoemporium.autoemporium.models.Currency;
import com.autoemporium.autoemporium.models.users.AccountType;
import com.autoemporium.autoemporium.dao.AdvertisementDAO;
import com.autoemporium.autoemporium.dao.SellerDAO;
import com.autoemporium.autoemporium.models.users.Role;
import com.autoemporium.autoemporium.models.users.Seller;
import com.autoemporium.autoemporium.models.users.User;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.GrantedAuthority;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.*;


@Service
@AllArgsConstructor
public class AdvertisementService {
    private final List<String> bannedWords = Arrays.asList("bad", "offensive", "word");
    private SellerDAO sellerDAO;
    private UserDAO userDAO;
    private UserService userService;
    private AdvertisementDAO advertisementDAO;
    private CarService carService;
    private MailService mailService;
    private CurrencyService currencyService;
    private AdvertisementViewDAO advertisementViewDAO;


    public ResponseEntity<String> saveByCarId(AdvertisementDTO advertisementDTO, Principal principal) {
        String username = principal.getName();
        Seller seller = sellerDAO.findSellerByUsername(username);

        if (seller.getAccountType() == AccountType.BASIC && seller.getCountOfAds() >= 1) {
            return new ResponseEntity<>("You can only post one advertisement with a basic account.", HttpStatus.FORBIDDEN);
        }

        Advertisement advertisement = createAdvertisement(advertisementDTO, seller);

        if (containsBannedWords(advertisementDTO.getTitle()) || containsBannedWords(advertisementDTO.getDescription())) {
            if (advertisementDTO.getCurrency() == Currency.UAH) {
                convertCurrency(advertisement);
            } else {
                setDefaultPriceCar(advertisement);
            }
            advertisement.setStatus(false);
            Advertisement savedAdvertisement = advertisementDAO.save(advertisement);
            int id = savedAdvertisement.getId();
            return new ResponseEntity<>("Your message contains banned words. Edit your message, id = " + id, HttpStatus.FORBIDDEN);
        }

        // Check if the currency is set to UAH
        if (advertisementDTO.getCurrency() == Currency.UAH) {
            convertCurrency(advertisement);
        } else {
            setDefaultPriceCar(advertisement);
        }

        Advertisement savedAdvertisement = advertisementDAO.save(advertisement);
        int id = savedAdvertisement.getId();

        return new ResponseEntity<>("Your advertisement has been saved. ID: " + id, HttpStatus.OK);
    }

    protected void convertCurrency(Advertisement advertisement) {
        BigDecimal sellRateUSD = currencyService.getCurrencyRate(Currency.USD.name());
        BigDecimal sellRateEUR = currencyService.getCurrencyRate(Currency.EUR.name());

        if (sellRateUSD != null && sellRateEUR != null) {
            BigDecimal priceUSD = advertisement.getPrice().divide(sellRateUSD, 2, RoundingMode.HALF_UP);
            BigDecimal priceEUR = advertisement.getPrice().divide(sellRateEUR, 2, RoundingMode.HALF_UP);

            PriceCar priceCar = new PriceCar();
            priceCar.setUAH(advertisement.getPrice());
            priceCar.setUSD(priceUSD);
            priceCar.setEUR(priceEUR);

            advertisement.setPriceCar(priceCar);
        } else {
            throw new RuntimeException("The sell rate for the specified currency is not available. Please choose a different currency.");
        }
    }

    protected void setDefaultPriceCar(Advertisement advertisement) {
        PriceCar priceCar = new PriceCar();
        priceCar.setUAH(BigDecimal.ZERO);
        priceCar.setUSD(BigDecimal.ZERO);
        priceCar.setEUR(BigDecimal.ZERO);

        advertisement.setPriceCar(priceCar);

        BigDecimal price = advertisement.getPrice();
        Currency currency = advertisement.getCurrency();
        BigDecimal sellRate = currencyService.getCurrencyRate(currency.name());

        if (sellRate != null) {
            BigDecimal priceUAH = price.multiply(sellRate);
            BigDecimal priceUSD = priceUAH.divide(currencyService.getCurrencyRate(Currency.USD.name()), 2, RoundingMode.HALF_UP);
            BigDecimal priceEUR = priceUAH.divide(currencyService.getCurrencyRate(Currency.EUR.name()), 2, RoundingMode.HALF_UP);

            advertisement.getPriceCar().setUAH(priceUAH);
            advertisement.getPriceCar().setUSD(priceUSD);
            advertisement.getPriceCar().setEUR(priceEUR);
        } else {
            throw new RuntimeException("The sell rate for the specified currency is not available. Please choose a different currency.");
        }
    }

    private Advertisement createAdvertisement(AdvertisementDTO advertisementDTO, Seller seller) {
        Advertisement advertisement = new Advertisement();
        advertisement.setTitle(advertisementDTO.getTitle());
        advertisement.setDescription(advertisementDTO.getDescription());
        advertisement.setCurrency(advertisementDTO.getCurrency());
        advertisement.setPrice(advertisementDTO.getPrice());
        Car car = carService.getCar(advertisementDTO.getCarId()).getBody();
        advertisement.setCar(car);
        advertisement.setStatus(true);
        advertisement.setCreatedAt(LocalDateTime.now().withNano(0));
        advertisement.setCreatedBySeller(seller);
        seller.setCountOfAds(seller.getCountOfAds() + 1);

        return advertisement;
    }


    public ResponseEntity<String> edit(int id, Advertisement advertisement, Principal principal) {
        String username = principal.getName();
        Seller seller = sellerDAO.findSellerByUsername(username);
        Advertisement existingAdvertisement = advertisementDAO.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid advertisement id"));
        Integer idCreatedBy = existingAdvertisement.getCreatedBySeller().getId();
        Integer idSeller = seller.getId();
        List<Role> roles = seller.getUser().getRoles();

        if (!idSeller.equals(idCreatedBy) && !roles.contains(Role.ADMIN) && !roles.contains(Role.MANAGER)) {
            return new ResponseEntity<>("You cannon edit this advertisement", HttpStatus.FORBIDDEN);
        }

        if (existingAdvertisement.getEditCount() >= 3 && !roles.contains(Role.ADMIN) && !roles.contains(Role.MANAGER)) {
            return new ResponseEntity<>("You massage id = " + id + " is banned, contact the administrator", HttpStatus.FORBIDDEN);
        }

        if (containsBannedWords(advertisement.getTitle()) || containsBannedWords(advertisement.getDescription())) {
            existingAdvertisement.setEditCount(existingAdvertisement.getEditCount() + 1);
            existingAdvertisement.setStatus(false);
            existingAdvertisement.setEditedAt(LocalDateTime.now().withNano(0));
            advertisementDAO.save(existingAdvertisement);
            int count = 3 - existingAdvertisement.getEditCount();

            if (count == 0 && !roles.contains(Role.ADMIN) && !roles.contains(Role.MANAGER)) {
                String managerEmail = userService.getManagerEmail();
                String subject = "Notification from Autoemporium: AdvertisementService";
                String body = "advertisement " + existingAdvertisement.toString() + "is banned";
                mailService.sendEmail(managerEmail, subject, body);
                return new ResponseEntity<>("You massage id = " + id + " is banned, contact the administrator", HttpStatus.FORBIDDEN);
            }

            return new ResponseEntity<>("You massage contains banned words. Edit massage, id = " + id + " You have " + count + " tries", HttpStatus.FORBIDDEN);
        }

        existingAdvertisement.setTitle(advertisement.getTitle());
        existingAdvertisement.setDescription(advertisement.getDescription());
        existingAdvertisement.setEditCount(existingAdvertisement.getEditCount() + 1);
        existingAdvertisement.setStatus(true);
        existingAdvertisement.setEditedAt(LocalDateTime.now().withNano(0));
        advertisementDAO.save(existingAdvertisement);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private boolean containsBannedWords(String text) {
        return bannedWords.stream().anyMatch(text::contains);
    }

    public ResponseEntity<List<Advertisement>> getAllAdvertisement() {
        List<Advertisement> all = advertisementDAO.findAll();
//        for (Advertisement advertisement : all) {
//            if (advertisement.getCurrency() == Currency.UAH) {
//                this.convertCurrency(advertisement);
//            } else {
//                this.setDefaultPriceCar(advertisement);
//            }
//        }
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    public ResponseEntity<Advertisement> getAdvertisementById(Integer id, Principal principal) {
        Advertisement advertisement = advertisementDAO.findById(id).orElse(null);
        if (advertisement != null) {
//            if (advertisement.getCurrency() == Currency.UAH) {
//                this.convertCurrency(advertisement);
//            } else {
//                this.setDefaultPriceCar(advertisement);
//            }

            String username = principal.getName();
            User user = userDAO.findByUsername(username);
            int idUser = user.getId();
            boolean isOwner = advertisement.getCreatedBySeller().getUser().getUsername().equals(username);

            AdvertisementView view = new AdvertisementView();
            view.setAdvertisement(advertisement);
            view.setViewDate(LocalDate.now());
            view.setViewerUserId(idUser );
            view.setIsOwner(isOwner);

            advertisement.setViews(advertisement.getViews() + 1);

            advertisementViewDAO.save(view);
        }
        return new ResponseEntity<>(advertisement, HttpStatus.OK);
    }

    public ResponseEntity<String> saveWithCar(AdvertisementDTO advertisementDTO, Principal principal) {
        String username = principal.getName();
        Seller seller = sellerDAO.findSellerByUsername(username);
        int sellerId = seller.getId();
//        ResponseEntity<User> userResponse = userService.getUserByUsername(username);
//        User user = userResponse.getBody();
//        List<String> authorities = userService.loadUserByUsername(username).getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        Advertisement advertisement = new Advertisement();
        CarDTO carDTO = advertisementDTO.getCarDTO();
        int producerId = carDTO.getProducerId();
        int modelId = carDTO.getModelId();
        String producer = carService.getProducerById(producerId).getBody();
        String model = carService.getModelByIdByProducerId(producerId, modelId).getBody();
        Car car = new Car();

        if (seller.getAccountType() == AccountType.BASIC && seller.getCountOfAds() >= 1) {
            return new ResponseEntity<>("You can only post one advertisement with a basic account.", HttpStatus.FORBIDDEN);
        } else if (containsBannedWords(advertisementDTO.getTitle()) || containsBannedWords(advertisementDTO.getDescription())) {

            advertisement.setStatus(false);
            advertisement.setCreatedAt(LocalDateTime.now().withNano(0));
            advertisement.setCreatedBySeller(seller);
            advertisement.getCar().setCreatedBySellerId(sellerId);
            advertisement.setTitle(advertisementDTO.getTitle());
            advertisement.setDescription(advertisementDTO.getDescription());
            advertisement.setCurrency(advertisementDTO.getCurrency());
            advertisement.setPrice(advertisementDTO.getPrice());
            seller.setCountOfAds(seller.getCountOfAds() + 1);
            car.setProducer(producer);
            car.setModel(model);
            car.setPower(carDTO.getPower());
            car.setYear(carDTO.getYear());
            car.setColor(carDTO.getColor());
            car.setMileage(carDTO.getMileage());
            car.setNumberSeats(carDTO.getNumberSeats());
            car.setNumberDoors(carDTO.getNumberDoors());
            car.setCreatedBySellerId(sellerId);
            advertisement.setCar(car);

            if (advertisementDTO.getCurrency() == Currency.UAH) {
                this.convertCurrency(advertisement);
            } else {
                this.setDefaultPriceCar(advertisement);
            }

            Advertisement savedAdvertisement = advertisementDAO.save(advertisement);
            int id = savedAdvertisement.getId();
            int carId = savedAdvertisement.getCar().getId();

            return new ResponseEntity<>("You massage contains banned words. Edit massage, id = " + id + " You car id = " + carId, HttpStatus.FORBIDDEN);
        } else {

            advertisement.setCreatedAt(LocalDateTime.now().withNano(0));
            advertisement.setStatus(true);
            advertisement.setCreatedBySeller(seller);
            advertisement.setTitle(advertisementDTO.getTitle());
            advertisement.setDescription(advertisementDTO.getDescription());
            advertisement.setCurrency(advertisementDTO.getCurrency());
            advertisement.setPrice(advertisementDTO.getPrice());
            car.setProducer(producer);
            car.setModel(model);
            car.setPower(carDTO.getPower());
            car.setYear(carDTO.getYear());
            car.setColor(carDTO.getColor());
            car.setMileage(carDTO.getMileage());
            car.setNumberSeats(carDTO.getNumberSeats());
            car.setNumberDoors(carDTO.getNumberDoors());
            car.setCreatedBySellerId(sellerId);
            advertisement.setCar(car);
            advertisementDAO.save(advertisement);

            if (advertisementDTO.getCurrency() == Currency.UAH) {
                this.convertCurrency(advertisement);
            } else {
                this.setDefaultPriceCar(advertisement);
            }

            Advertisement savedAdvertisement = advertisementDAO.save(advertisement);
            int id = savedAdvertisement.getId();
            int carId = savedAdvertisement.getCar().getId();

            return new ResponseEntity<>("You massage id = " + id + " You car id = " + carId, HttpStatus.OK);
        }
    }

    public ResponseEntity<String> saveWithCarWithPhoto(Principal principal, int modelId, int producerId, int power, MultipartFile[] photos, int year, String color, int mileage, int numberDoors,
                                                       int numberSeats, String title, String description, BigDecimal price, Currency currency
    ) throws IOException {
        String username = principal.getName();
        Seller seller = sellerDAO.findSellerByUsername(username);
        int sellerId = seller.getId();
//        ResponseEntity<User> userResponse = userService.getUserByUsername(username);
//        User user = userResponse.getBody();
//        List<String> authorities = userService.loadUserByUsername(username).getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        Advertisement advertisement = new Advertisement();
        String producer = carService.getProducerById(producerId).getBody();
        String model = carService.getModelByIdByProducerId(producerId, modelId).getBody();
        Car car = new Car();

        if (seller.getAccountType() == AccountType.BASIC && seller.getCountOfAds() >= 1) {
            return new ResponseEntity<>("You can only post one advertisement with a basic account.", HttpStatus.FORBIDDEN);
        } else if (containsBannedWords(title) || containsBannedWords(description)) {

            advertisement.setStatus(false);
            advertisement.setCreatedAt(LocalDateTime.now().withNano(0));
            advertisement.setCreatedBySeller(seller);
            advertisement.setTitle(title);
            advertisement.setDescription(description);
            advertisement.setCurrency(currency);
            advertisement.setPrice(price);
            seller.setCountOfAds(seller.getCountOfAds() + 1);
            car.setProducer(producer);
            car.setModel(model);
            car.setPower(power);
            car.setYear(year);
            car.setColor(color);
            car.setMileage(mileage);
            car.setNumberSeats(numberSeats);
            car.setNumberDoors(numberDoors);
            car.setCreatedBySellerId(sellerId);
            List<String> photoPaths = new ArrayList<>();

            for (MultipartFile photo : photos) {
                String originalFilename = photo.getOriginalFilename();
                String photoPath = "/photo/" + originalFilename;
                photoPaths.add(photoPath);
                String path = System.getProperty("user.home") + File.separator + "images" + File.separator + originalFilename;
                File file = new File(path);
                photo.transferTo(file);
            }
            car.setPhoto(photoPaths);
            advertisement.setCar(car);

            if (currency == Currency.UAH) {
                this.convertCurrency(advertisement);
            } else {
                this.setDefaultPriceCar(advertisement);
            }

            Advertisement savedAdvertisement = advertisementDAO.save(advertisement);
            int id = savedAdvertisement.getId();
            int carId = savedAdvertisement.getCar().getId();

            return new ResponseEntity<>("You massage contains banned words. Edit massage, id = " + id + " You car id = " + carId, HttpStatus.FORBIDDEN);
        } else {

            advertisement.setCreatedAt(LocalDateTime.now().withNano(0));
            advertisement.setStatus(true);
            advertisement.setCreatedBySeller(seller);
            advertisement.setTitle(title);
            advertisement.setDescription(description);
            advertisement.setCurrency(currency);
            advertisement.setPrice(price);
            car.setProducer(producer);
            car.setModel(model);
            car.setPower(power);
            car.setYear(year);
            car.setColor(color);
            car.setMileage(mileage);
            car.setNumberSeats(numberSeats);
            car.setNumberDoors(numberDoors);
            car.setCreatedBySellerId(sellerId);
            List<String> photoPaths = new ArrayList<>();

            for (MultipartFile photo : photos) {
                String originalFilename = photo.getOriginalFilename();
                String photoPath = "/photo/" + originalFilename;
                photoPaths.add(photoPath);
                String path = System.getProperty("user.home") + File.separator + "images" + File.separator + originalFilename;
                File file = new File(path);
                photo.transferTo(file);
            }
            car.setPhoto(photoPaths);

            advertisement.setCar(car);
            advertisementDAO.save(advertisement);

            if (currency == Currency.UAH) {
                this.convertCurrency(advertisement);
            } else {
                this.setDefaultPriceCar(advertisement);
            }

            Advertisement savedAdvertisement = advertisementDAO.save(advertisement);
            int id = savedAdvertisement.getId();
            int carId = savedAdvertisement.getCar().getId();

            return new ResponseEntity<>("You massage id = " + id + " You car id = " + carId, HttpStatus.OK);
        }
    }

    public ResponseEntity<String> deleteAdvertisementById(int id, Principal principal) {
        Advertisement adv = advertisementDAO.findById(id).orElse(null);
        advertisementDAO.delete(adv);
        return new ResponseEntity<>("Advertisement is deleted", HttpStatus.OK);

//        if (id > 0) {
//            String username = principal.getName();
//            Advertisement adv = advertisementDAO.findById(id).orElse(null);
//            if (adv == null) {
//                return new ResponseEntity<>("Advertisement not found", HttpStatus.NOT_FOUND);
//            }
//
//            User user = userDAO.findByUsername(username);
//
//            Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
//            boolean isSeller = false;
//            boolean isAdmin = false;
//
//            for (GrantedAuthority authority : authorities) {
//                String authorityName = authority.getAuthority();
//                if ("SELLER".equals(authorityName)) {
//                    isSeller = true;
//                    break;
//                }
//                if ("ADMIN".equals(authorityName) || "MANAGER".equals(authorityName)) {
//                    isAdmin = true;
//                    break;
//                }
//            }
//
//            boolean isCreatedByUser = adv.getCreatedBySeller().getUser().getUsername().equals(username);
//
//            if (isAdmin || (isSeller && isCreatedByUser)) {
//                advertisementDAO.delete(adv);
//                return new ResponseEntity<>("Advertisement is deleted", HttpStatus.OK);
//            }
//
//            return new ResponseEntity<>("You cannot delete this advertisement", HttpStatus.FORBIDDEN);
//        }
//        return new ResponseEntity<>("Advertisement id < 0)", HttpStatus.FORBIDDEN);
    }
}

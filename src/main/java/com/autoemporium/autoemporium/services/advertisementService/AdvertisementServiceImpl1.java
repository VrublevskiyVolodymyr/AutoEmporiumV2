package com.autoemporium.autoemporium.services.advertisementService;


import com.autoemporium.autoemporium.dao.*;
import com.autoemporium.autoemporium.models.advertisement.Advertisement;
import com.autoemporium.autoemporium.models.advertisement.AdvertisementDTO;
import com.autoemporium.autoemporium.models.advertisement.AdvertisementView;
import com.autoemporium.autoemporium.models.financial.Currency;
import com.autoemporium.autoemporium.models.autodealer.AutoDealer;
import com.autoemporium.autoemporium.models.cars.Car;
import com.autoemporium.autoemporium.models.cars.CarDTO;
import com.autoemporium.autoemporium.models.users.AccountType;
import com.autoemporium.autoemporium.models.users.Role;
import com.autoemporium.autoemporium.models.users.Seller;
import com.autoemporium.autoemporium.models.users.User;
import com.autoemporium.autoemporium.services.carService.CarService;
import com.autoemporium.autoemporium.services.mailService.MailService;
import com.autoemporium.autoemporium.services.userService.UserService;
import com.autoemporium.autoemporium.services.financialServices.CurrencyService;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.GrantedAuthority;

import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.*;


@Service
@AllArgsConstructor
public class AdvertisementServiceImpl1 implements AdvertisementService {
    private final List<String> bannedWords = Arrays.asList("bad", "offensive", "word");
    private SellerDAO sellerDAO;
    private UserDAO userDAO;
    private UserService userService;
    private AdvertisementDAO advertisementDAO;
    private CarService carService;
    private MailService mailService;
    private CurrencyService currencyService;
    private AdvertisementViewDAO advertisementViewDAO;
    private RegionDAO regionDAO;
    private AutoDealerDAO autoDealerDAO;


    public ResponseEntity<String> saveByCarId(AdvertisementDTO advertisementDTO, Principal principal) {

        Advertisement existingAdvertisement = advertisementDAO.findFirstByTitleAndDescription(advertisementDTO.getTitle(), advertisementDTO.getDescription());
        if (existingAdvertisement != null) {
            return new ResponseEntity<>("A similar advertisement already exists with ID: " + existingAdvertisement.getId(), HttpStatus.CONFLICT);
        }

        String username = principal.getName();
        Seller seller = sellerDAO.findSellerByUsername(username);

        Advertisement existAdv = advertisementDAO.findByCarId(advertisementDTO.getCarId());
        if (existAdv != null) {
            return new ResponseEntity<>("An ad with the specified carId already exists with the ID: " + existAdv.getId(), HttpStatus.CONFLICT);
        }

        if (seller.getAccountType() == AccountType.BASIC && seller.getCountOfAds() >= 1) {
            return new ResponseEntity<>("You can only post one advertisement with a basic account.", HttpStatus.FORBIDDEN);
        }

        Advertisement advertisement = createAdvertisement(advertisementDTO, seller);

        if (containsBannedWords(advertisementDTO.getTitle()) || containsBannedWords(advertisementDTO.getDescription())) {
            this.convertCurrency(advertisement, advertisementDTO.getCurrency());

            advertisement.setStatus(false);
            Advertisement savedAdvertisement = advertisementDAO.save(advertisement);
            int id = savedAdvertisement.getId();
            return new ResponseEntity<>("Your advertisement contains banned words. Edit your message, id = " + id, HttpStatus.FORBIDDEN);
        }

        this.convertCurrency(advertisement, advertisementDTO.getCurrency());

        Advertisement savedAdvertisement = advertisementDAO.save(advertisement);
        int id = savedAdvertisement.getId();

        return new ResponseEntity<>("Your advertisement has been saved. ID: " + id, HttpStatus.OK);
    }


    private Advertisement createAdvertisement(AdvertisementDTO advertisementDTO, Seller seller) {
        Advertisement advertisement = new Advertisement();
        String region = regionDAO.findById(advertisementDTO.getRegionId()).get().getRegion();

        advertisement.setTitle(advertisementDTO.getTitle());
        advertisement.setDescription(advertisementDTO.getDescription());
        advertisement.setCurrency(advertisementDTO.getCurrency());
        advertisement.setPrice(advertisementDTO.getPrice());
        Car car = carService.getCar(advertisementDTO.getCarId()).getBody();
        advertisement.setCar(car);
        advertisement.setStatus(true);
        advertisement.setCreatedAt(LocalDateTime.now().withNano(0));
        advertisement.setCreatedBySeller(seller);
        advertisement.setRegion(region);

        if (seller.getUser().getRoles().contains(Role.DEALER_SELLER)) {
            AutoDealer autoDealer = autoDealerDAO.findAutoDealerBySalesIsContaining(seller);
            advertisement.setCreatedByDealer(autoDealer);
        }

        seller.setCountOfAds(seller.getCountOfAds() + 1);

        return advertisement;
    }

    public ResponseEntity<String> saveWithCar(AdvertisementDTO advertisementDTO, Principal principal) {
        Advertisement existingAdvertisement = advertisementDAO.findFirstByTitleAndDescription(advertisementDTO.getTitle(), advertisementDTO.getDescription());
        if (existingAdvertisement != null) {
            return new ResponseEntity<>("A similar advertisement already exists with ID: " + existingAdvertisement.getId(), HttpStatus.CONFLICT);
        }

        String username = principal.getName();
        Seller seller = sellerDAO.findSellerByUsername(username);
        int sellerId = seller.getId();
        Advertisement advertisement = new Advertisement();
        String region = regionDAO.findById(advertisementDTO.getRegionId()).get().getRegion();
        CarDTO carDTO = advertisementDTO.getCarDTO();

        if (seller.getAccountType() == AccountType.BASIC && seller.getCountOfAds() >= 1) {
            return new ResponseEntity<>("You can only post one advertisement with a basic account.", HttpStatus.FORBIDDEN);
        } else if (containsBannedWords(advertisementDTO.getTitle()) || containsBannedWords(advertisementDTO.getDescription())) {

            advertisement.setStatus(false);
            advertisement.setCreatedAt(LocalDateTime.now().withNano(0));
            advertisement.setCreatedBySeller(seller);
//            advertisement.getCar().setCreatedBySellerId(sellerId);
            advertisement.setTitle(advertisementDTO.getTitle());
            advertisement.setDescription(advertisementDTO.getDescription());
            advertisement.setCurrency(advertisementDTO.getCurrency());
            advertisement.setPrice(advertisementDTO.getPrice());
            advertisement.setRegion(region);
            seller.setCountOfAds(seller.getCountOfAds() + 1);
            Car car = this.createCar(carDTO, sellerId);
            advertisement.setCar(car);

            if (seller.getUser().getRoles().contains(Role.DEALER_SELLER)) {
                AutoDealer autoDealer = autoDealerDAO.findAutoDealerBySalesIsContaining(seller);
                advertisement.setCreatedByDealer(autoDealer);
            }

            this.convertCurrency(advertisement, advertisementDTO.getCurrency());

            Advertisement savedAdvertisement = advertisementDAO.save(advertisement);
            int id = savedAdvertisement.getId();
            int carId = savedAdvertisement.getCar().getId();

            return new ResponseEntity<>("You advertisement contains banned words. Edit massage, id = " + id + " You car id = " + carId, HttpStatus.FORBIDDEN);
        } else {

            advertisement.setCreatedAt(LocalDateTime.now().withNano(0));
            advertisement.setStatus(true);
            advertisement.setCreatedBySeller(seller);
            advertisement.setTitle(advertisementDTO.getTitle());
            advertisement.setDescription(advertisementDTO.getDescription());
            advertisement.setCurrency(advertisementDTO.getCurrency());
            advertisement.setPrice(advertisementDTO.getPrice());
            advertisement.setRegion(region);
            seller.setCountOfAds(seller.getCountOfAds() + 1);

            Car car = this.createCar(carDTO, sellerId);
            advertisement.setCar(car);

            if (seller.getUser().getRoles().contains(Role.DEALER_SELLER)) {
                AutoDealer autoDealer = autoDealerDAO.findAutoDealerBySalesIsContaining(seller);
                advertisement.setCreatedByDealer(autoDealer);
            }

            advertisementDAO.save(advertisement);

            this.convertCurrency(advertisement, advertisementDTO.getCurrency());

            Advertisement savedAdvertisement = advertisementDAO.save(advertisement);
            int id = savedAdvertisement.getId();
            int carId = savedAdvertisement.getCar().getId();

            return new ResponseEntity<>("You advertisement id = " + id + " You car id = " + carId, HttpStatus.OK);
        }
    }

    public ResponseEntity<String> saveWithCarWithPhoto(Principal principal, int modelId, int producerId, int power, MultipartFile[] photos, int year, String color, int mileage, int numberDoors,
                                                       int numberSeats, String title, String description, BigDecimal price, Currency currency, int regionId
    ) throws IOException {

        Advertisement existingAdvertisement = advertisementDAO.findFirstByTitleAndDescription(title, description);
        if (existingAdvertisement != null) {
            return new ResponseEntity<>("A similar advertisement already exists with ID: " + existingAdvertisement.getId(), HttpStatus.CONFLICT);
        }

        String username = principal.getName();
        Seller seller = sellerDAO.findSellerByUsername(username);
        int sellerId = seller.getId();

        Advertisement advertisement = new Advertisement();
        String producer = carService.getProducerById(producerId).getBody();
        String model = carService.getModelByIdByProducerId(producerId, modelId).getBody();
        String region = regionDAO.findById(regionId).get().getRegion();

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
            advertisement.setRegion(region);

            if (seller.getUser().getRoles().contains(Role.DEALER_SELLER)) {
                AutoDealer autoDealer = autoDealerDAO.findAutoDealerBySalesIsContaining(seller);
                advertisement.setCreatedByDealer(autoDealer);
            }

            seller.setCountOfAds(seller.getCountOfAds() + 1);
            Car car = new Car(producer, model, power, year, color, mileage, numberSeats, numberDoors, sellerId);

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

            this.convertCurrency(advertisement, currency);

            Advertisement savedAdvertisement = advertisementDAO.save(advertisement);
            int id = savedAdvertisement.getId();
            int carId = savedAdvertisement.getCar().getId();

            return new ResponseEntity<>("You advertisement contains banned words. Edit massage, id = " + id + " You car id = " + carId, HttpStatus.FORBIDDEN);
        } else {

            advertisement.setCreatedAt(LocalDateTime.now().withNano(0));
            advertisement.setStatus(true);
            advertisement.setCreatedBySeller(seller);
            advertisement.setTitle(title);
            advertisement.setDescription(description);
            advertisement.setCurrency(currency);
            advertisement.setPrice(price);
            advertisement.setRegion(region);

            if (seller.getUser().getRoles().contains(Role.DEALER_SELLER)) {
                AutoDealer autoDealer = autoDealerDAO.findAutoDealerBySalesIsContaining(seller);
                advertisement.setCreatedByDealer(autoDealer);
            }

            seller.setCountOfAds(seller.getCountOfAds() + 1);

            Car car = new Car(producer, model, power, year, color, mileage, numberSeats, numberDoors, sellerId);

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

            this.convertCurrency(advertisement, currency);

            Advertisement savedAdvertisement = advertisementDAO.save(advertisement);
            int id = savedAdvertisement.getId();
            int carId = savedAdvertisement.getCar().getId();

            return new ResponseEntity<>("You advertisement id = " + id + " You car id = " + carId, HttpStatus.OK);
        }
    }


    public ResponseEntity<List<Advertisement>> getAllAvailableAdvertisement() {
        List<Advertisement> all = advertisementDAO.findByStatus(true);

        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    public ResponseEntity<List<Advertisement>> getAllAdvertisement() {
        List<Advertisement> all = advertisementDAO.findAll();

        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Advertisement>> getAllAdvByAutoDealers(int id) {
        try {
            List<Advertisement> advByAutoDealers = advertisementDAO.findAllByCreatedByDealerId(id);
            return new ResponseEntity<>(advByAutoDealers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Advertisement> getAvailableAdvertisementById(Integer id, Principal principal) {
        Advertisement advertisement = advertisementDAO.findByIdAndStatus(id, true);
        if (advertisement != null) {

            String username = principal.getName();
            User user = userDAO.findByUsername(username);
            int idUser = user.getId();
            boolean isOwner = advertisement.getCreatedBySeller().getUser().getUsername().equals(username);

            AdvertisementView view = new AdvertisementView();
            view.setAdvertisement(advertisement);
            view.setViewDate(LocalDate.now());
            view.setViewerUserId(idUser);
            view.setIsOwner(isOwner);

            advertisement.setViews(advertisement.getViews() + 1);

            advertisementViewDAO.save(view);
            return new ResponseEntity<>(advertisement, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.valueOf("Not exist"));
    }

    public ResponseEntity<Advertisement> getAdvertisementById(Integer id, Principal principal) {
        Advertisement advertisement = advertisementDAO.findById(id).orElse(null);
        if (advertisement != null) {

            String username = principal.getName();
            User user = userDAO.findByUsername(username);
            int idUser = user.getId();
            boolean isOwner = advertisement.getCreatedBySeller().getUser().getUsername().equals(username);

            AdvertisementView view = new AdvertisementView();
            view.setAdvertisement(advertisement);
            view.setViewDate(LocalDate.now());
            view.setViewerUserId(idUser);
            view.setIsOwner(isOwner);

            advertisement.setViews(advertisement.getViews() + 1);

            advertisementViewDAO.save(view);
            return new ResponseEntity<>(advertisement, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.valueOf("Not exist"));
    }


    public ResponseEntity<List<Advertisement>> getAdvByCarPower(int value, Principal principal) {
        String username = null;
        int idUser = 0;

        if (principal != null) {
            username = principal.getName();
            User user = userDAO.findByUsername(username);
            idUser = user.getId();
        }

        List<Advertisement> advertisements = advertisementDAO.findByStatusAndCar_Power(true, value);
        for (Advertisement advertisement : advertisements) {
            updateAdvertisementViews(advertisement, username, idUser);
        }
        return new ResponseEntity<>(advertisements, HttpStatus.OK);
    }

    public ResponseEntity<List<Advertisement>> getAdvByCarProducer(Integer producerId, Principal principal) {
        String username = null;
        int idUser = 0;

        if (principal != null) {
            username = principal.getName();
            User user = userDAO.findByUsername(username);
            idUser = user.getId();
        }

        String producer = carService.getProducerById(producerId).getBody();
        List<Advertisement> advertisements = advertisementDAO.findByStatusAndCar_Producer(true, producer);
        for (Advertisement advertisement : advertisements) {
            updateAdvertisementViews(advertisement, username, idUser);
        }
        return new ResponseEntity<>(advertisements, HttpStatus.OK);
    }

    public ResponseEntity<List<Advertisement>> getAdvByCarProducerAndCarModel(Integer producerId, Integer modelId, Principal principal) {
        String username = null;
        int idUser = 0;

        if (principal != null) {
            username = principal.getName();
            User user = userDAO.findByUsername(username);
            idUser = user.getId();
        }

        String producer = carService.getProducerById(producerId).getBody();
        String model = carService.getModelByIdByProducerId(producerId, modelId).getBody();
        List<Advertisement> advertisements = advertisementDAO.findByStatusAndCar_ProducerAndCar_Model(true, producer, model);
        for (Advertisement advertisement : advertisements) {
            updateAdvertisementViews(advertisement, username, idUser);
        }
        return new ResponseEntity<>(advertisements, HttpStatus.OK);
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
            return new ResponseEntity<>("You advertisement id = " + id + " is banned, contact the administrator", HttpStatus.FORBIDDEN);
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
                return new ResponseEntity<>("You advertisement id = " + id + " is banned, contact the administrator", HttpStatus.FORBIDDEN);
            }

            return new ResponseEntity<>("You advertisement contains banned words. Edit massage, id = " + id + " You have " + count + " tries", HttpStatus.FORBIDDEN);
        }

        existingAdvertisement.setTitle(advertisement.getTitle());
        existingAdvertisement.setDescription(advertisement.getDescription());
        existingAdvertisement.setEditCount(existingAdvertisement.getEditCount() + 1);
        existingAdvertisement.setStatus(true);
        existingAdvertisement.setEditedAt(LocalDateTime.now().withNano(0));
        advertisementDAO.save(existingAdvertisement);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    public ResponseEntity<String> deleteAdvertisementById(int id, Principal principal) {

        if (id <= 0) {
            return new ResponseEntity<>("Invalid Advertisement Id", HttpStatus.BAD_REQUEST);
        }

        String username = principal.getName();
        Advertisement adv = advertisementDAO.findById(id).orElse(null);
        if (adv == null) {
            return new ResponseEntity<>("Advertisement not found", HttpStatus.NOT_FOUND);
        }

        User user = userDAO.findByUsername(username);

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        boolean isSeller = false;
        boolean isAdmin = false;

        for (GrantedAuthority authority : authorities) {
            String authorityName = authority.getAuthority();
            if ("SELLER".equals(authorityName)) {
                isSeller = true;
                break;
            }
            if ("ADMIN".equals(authorityName) || "MANAGER".equals(authorityName)) {
                isAdmin = true;
                break;
            }
        }

        boolean isCreatedByUser = adv.getCreatedBySeller().getUser().getUsername().equals(username);

        if (isAdmin || (isSeller && isCreatedByUser)) {
            adv.getCreatedBySeller().getAdvertisements().remove(adv);
            advertisementDAO.delete(adv);
            return new ResponseEntity<>("Advertisement is deleted", HttpStatus.OK);
        }

        return new ResponseEntity<>("You cannot delete this advertisement", HttpStatus.FORBIDDEN);
    }


    private void convertCurrency(Advertisement advertisement, Currency currency) {
        if (currency == Currency.UAH) {
            currencyService.convertCurrency(advertisement);
        } else {
            currencyService.setDefaultPriceCar(advertisement);
        }
    }

    private Car createCar(CarDTO carDTO, int sellerId) {
        String producer = carService.getProducerById(carDTO.getProducerId()).getBody();
        String model = carService.getModelByIdByProducerId(carDTO.getProducerId(), carDTO.getModelId()).getBody();

        return new Car(producer, model, carDTO.getPower(), carDTO.getYear(), carDTO.getColor(), carDTO.getMileage(),
                carDTO.getNumberSeats(), carDTO.getNumberDoors(), sellerId);
    }

    private void updateAdvertisementViews(Advertisement advertisement, String username, int userId) {
        boolean isOwner = false;
        User createdByUser = advertisement.getCreatedBySeller().getUser();
        AutoDealer createdByDealer = advertisement.getCreatedByDealer();

        if (userId != 0 && createdByUser != null) {
            String createdByUsername = createdByUser.getUsername();
            isOwner = createdByUsername.equals(username);
        }

        if (createdByDealer != null) {
            isOwner = createdByDealer.getName().equals(username);
        }

        AdvertisementView view = new AdvertisementView();
        view.setAdvertisement(advertisement);
        view.setViewDate(LocalDate.now());
        view.setViewerUserId(userId);
        view.setIsOwner(isOwner);
        advertisement.setViews(advertisement.getViews() + 1);
        advertisementViewDAO.save(view);
    }

    private boolean containsBannedWords(String text) {
        return bannedWords.stream().anyMatch(word -> text.toLowerCase().contains(word.toLowerCase()));
    }
}

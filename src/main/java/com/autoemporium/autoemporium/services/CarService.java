package com.autoemporium.autoemporium.services;

import com.autoemporium.autoemporium.dao.*;
import com.autoemporium.autoemporium.models.*;
import com.autoemporium.autoemporium.models.users.Role;
import com.autoemporium.autoemporium.models.users.Seller;
import com.autoemporium.autoemporium.models.users.User;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Service
@AllArgsConstructor
public class CarService {
    private CarDAO carDAO;
    private SellerDAO sellerDAO;
    private ProducerDAO producerDAO;
    private UserDAO userDAO;
    private RegionDAO regionDAO;


    public ResponseEntity<String> save(CarDTO carDTO, Principal principal) {
        if (carDTO == null) {
            throw new RuntimeException();
        }
        String username = principal.getName();
        Seller seller = sellerDAO.findSellerByUsername(username);
        int sellerId = seller.getId();
        int producerId = (carDTO.getProducerId());
        String producer = getProducerById(producerId).getBody();
        String model = getModelByIdByProducerId(carDTO.getProducerId(), carDTO.getModelId()).getBody();
        Car car = new Car();
        car.setProducer(producer);
        car.setModel(model);
        car.setColor(carDTO.getColor());
        car.setYear(carDTO.getYear());
        car.setPower(carDTO.getPower());
        car.setMileage(carDTO.getMileage());
        car.setNumberDoors(carDTO.getNumberDoors());
        car.setNumberSeats(carDTO.getNumberSeats());
        car.setPhoto(carDTO.getPhoto());
        car.setCreatedBySellerId(sellerId);
        Car savedCar = carDAO.save(car);
        int id = savedCar.getId();
        return new ResponseEntity<>("You car id = " + id, HttpStatus.OK);
    }

    public ResponseEntity<List<Car>> getAllCars() {
        Sort by = Sort.by(Sort.Order.asc("id"));
        return new ResponseEntity<>(carDAO.findAll(by), HttpStatus.OK);
    }

    public ResponseEntity<List<Car>> findAllWithSpecifications(Specification<Car> criteria) {
        List<Car> all = carDAO.findAll(criteria);
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    public ResponseEntity<Car> getCar(int id) {
        Car car = null;
        if (id > 0) {
            car = carDAO.findById(id).get();
        }
        return new ResponseEntity<>(car, HttpStatus.OK);
    }

    public ResponseEntity<List<Producer>> getAllProducers() {
        List<Producer> all = producerDAO.findAll();
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    public ResponseEntity<List<Model>> getAllModels(Integer producerId) {
        Producer producer = producerDAO.findById(producerId).orElseThrow(() -> new IllegalArgumentException("Producer not found"));
        List<Model> models = producer.getModels();
        return new ResponseEntity<>(models, HttpStatus.OK);
    }

    public ResponseEntity<String> getProducerById(Integer id) {
        String producer = producerDAO.findById(id).map(Producer::getProducer).orElse(null);
        return new ResponseEntity<>(producer, HttpStatus.OK);
    }

    public ResponseEntity<String> getModelByIdByProducerId(Integer producerId, Integer modelId) {
        String model = producerDAO.findById(producerId).orElse(null).getModels().stream().filter(model1 -> model1.getId() == modelId).findFirst().map(Model::getModel).orElse(null);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    public ResponseEntity<String> deleteCarById(int id, Principal principal) {
        if (id > 0) {
            String username = principal.getName();
            Car c = carDAO.findById(id).orElse(null);
            if (c == null) {
                return new ResponseEntity<>("Car not found", HttpStatus.NOT_FOUND);
            }
            Advertisement advertisement = c.getAdvertisement();

            if (advertisement != null) {
                return new ResponseEntity<>("You cannot delete this car because it is used in an ad id " + advertisement.getId(), HttpStatus.FORBIDDEN);
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

            if (isAdmin || (isSeller && c.getCreatedBySellerId() == sellerDAO.findSellerByUsername(username).getId())) {
                carDAO.delete(c);
                return new ResponseEntity<>("Car is deleted", HttpStatus.OK);
            }

            return new ResponseEntity<>("You cannot delete this car", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(" Car id < 0)", HttpStatus.FORBIDDEN);
    }


    public ResponseEntity<String> updateCar(int id, CarDTO carDTO, Principal principal) {

        String username = principal.getName();
        Car c = carDAO.findById(id).orElse(null);
        if (c == null) {
            return new ResponseEntity<>("Car not found", HttpStatus.NOT_FOUND);
        }

        String producer = getProducerById(carDTO.getProducerId()).getBody();
        String model = getModelByIdByProducerId(carDTO.getProducerId(), carDTO.getModelId()).getBody();

        c.setModel(model);
        c.setProducer(producer);
        c.setPower(carDTO.getPower());
        c.setYear(carDTO.getYear());
        c.setColor(carDTO.getColor());
        c.setMileage(carDTO.getMileage());
        c.setNumberDoors(carDTO.getNumberDoors());
        c.setNumberSeats(carDTO.getNumberSeats());

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

        if (isAdmin || (isSeller && c.getCreatedBySellerId() == sellerDAO.findSellerByUsername(username).getId())) {
            carDAO.save(c);
            return new ResponseEntity<>("Car is updated", HttpStatus.OK);
        }

        return new ResponseEntity<>("You cannot update this car", HttpStatus.FORBIDDEN);
    }

    public ResponseEntity<List<Car>> getCarsByPower(int value) {
        return new ResponseEntity<>(carDAO.findByPower(value), HttpStatus.OK);
    }

    public ResponseEntity<List<Car>> getCarsByProducer(Integer id) {
        String producer = getProducerById(id).getBody();
        return new ResponseEntity<>(carDAO.findByProducer(producer), HttpStatus.OK);
    }

    public ResponseEntity<String> saveWithPhotos(int producerId, int modelId, int power, MultipartFile[] photos, int year, String color, int mileage,
                                                 int numberDoors, int numberSeats, Principal principal
    ) throws IOException {
        String username = principal.getName();
        Seller seller = sellerDAO.findSellerByUsername(username);
        int sellerId = seller.getId();
        String producer = getProducerById(producerId).getBody();
        String model = getModelByIdByProducerId(producerId, modelId).getBody();

        Car car = new Car(producer, model, power, year, color, mileage, numberDoors, numberSeats);
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
        Car savedCar = carDAO.save(car);
        int id = savedCar.getId();
        return new ResponseEntity<>("You car id = " + id, HttpStatus.OK);
    }


    public void savePhotoToCarId(int id, MultipartFile[] photos) throws IOException {
        Car car = getCar(id).getBody();
        assert car != null;
        List<String> photoPaths = new ArrayList<>();
        for (MultipartFile photo : photos) {
            String originalFilename = photo.getOriginalFilename();
            String photoPath = "/photo/" + originalFilename;
            photoPaths.add(photoPath);
            String path = System.getProperty("user.home") + File.separator + "images" + File.separator + originalFilename;
            File file = new File(path);
            photo.transferTo(file);
        }

        car.getPhoto().addAll(photoPaths);
        carDAO.save(car);
    }

    public ResponseEntity<List<Region>> getAllRegions() {
        List<Region> all = regionDAO.findAll();
        return new ResponseEntity<>(all, HttpStatus.OK);
    }


    public ResponseEntity<Region> getRegionById(Integer id) {
        Region region = regionDAO.findById(id).get();
        System.out.println(region);
        return new ResponseEntity<>(region, HttpStatus.OK);
    }
}

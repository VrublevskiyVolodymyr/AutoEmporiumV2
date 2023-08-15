package com.autoemporium.autoemporium.services.carService;

import com.autoemporium.autoemporium.models.cars.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

public interface CarService {
    public ResponseEntity<String> save(CarDTO carDTO, Principal principal);

    public ResponseEntity<String> saveWithPhotos(int producerId, int modelId, int power, MultipartFile[] photos, int year, String color, int mileage,
                                                 int numberDoors, int numberSeats, Principal principal) throws IOException;

    public void savePhotoToCarId(int id, MultipartFile[] photos) throws IOException;

    public ResponseEntity<String> deleteCarById(int id, Principal principal);

    public ResponseEntity<String> updateCar(int id, CarDTO carDTO, Principal principal);

    public ResponseEntity<List<Car>> getAllCars();

    public ResponseEntity<List<Car>> findAllWithSpecifications(Specification<Car> criteria);

    public ResponseEntity<Car> getCar(int id);

    public ResponseEntity<List<Producer>> getAllProducers();

    public ResponseEntity<List<Model>> getAllModels(Integer producerId);

    public ResponseEntity<String> getProducerById(Integer id);

    public ResponseEntity<String> getModelByIdByProducerId(Integer producerId, Integer modelId);

    public ResponseEntity<List<Car>> getCarsByPower(int value);

    public ResponseEntity<List<Car>> getCarsByProducer(Integer id);

    public ResponseEntity<List<Region>> getAllRegions();

    public ResponseEntity<Region> getRegionById(Integer id);


}

package com.autoemporium.autoemporium.services.carService;

import com.autoemporium.autoemporium.models.cars.*;
import com.autoemporium.autoemporium.services.carService.CarService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

public class CarServiceImpl2 implements CarService {
    @Override
    public ResponseEntity<String> save(CarDTO carDTO, Principal principal) {
        return null;
    }

    @Override
    public ResponseEntity<String> saveWithPhotos(int producerId, int modelId, int power, MultipartFile[] photos, int year, String color, int mileage, int numberDoors, int numberSeats, Principal principal) throws IOException {
        return null;
    }

    @Override
    public void savePhotoToCarId(int id, MultipartFile[] photos) throws IOException {

    }

    @Override
    public ResponseEntity<String> deleteCarById(int id, Principal principal) {
        return null;
    }

    @Override
    public ResponseEntity<String> updateCar(int id, CarDTO carDTO, Principal principal) {
        return null;
    }

    @Override
    public ResponseEntity<List<Car>> getAllCars() {
        return null;
    }

    @Override
    public ResponseEntity<List<Car>> findAllWithSpecifications(Specification<Car> criteria) {
        return null;
    }

    @Override
    public ResponseEntity<Car> getCar(int id) {
        return null;
    }

    @Override
    public ResponseEntity<List<Producer>> getAllProducers() {
        return null;
    }

    @Override
    public ResponseEntity<List<Model>> getAllModels(Integer producerId) {
        return null;
    }

    @Override
    public ResponseEntity<String> getProducerById(Integer id) {
        return null;
    }

    @Override
    public ResponseEntity<String> getModelByIdByProducerId(Integer producerId, Integer modelId) {
        return null;
    }

    @Override
    public ResponseEntity<List<Car>> getCarsByPower(int value) {
        return null;
    }

    @Override
    public ResponseEntity<List<Car>> getCarsByProducer(Integer id) {
        return null;
    }

    @Override
    public ResponseEntity<List<Region>> getAllRegions() {
        return null;
    }

    @Override
    public ResponseEntity<Region> getRegionById(Integer id) {
        return null;
    }
}

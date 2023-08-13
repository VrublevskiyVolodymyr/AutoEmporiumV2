package com.autoemporium.autoemporium.controllers;



import com.autoemporium.autoemporium.dao.CarDAO;
import com.autoemporium.autoemporium.models.*;
import com.autoemporium.autoemporium.queryFilters.CarSpecifications;
import com.autoemporium.autoemporium.services.CarService;
import com.autoemporium.autoemporium.views.Views;
import com.fasterxml.jackson.annotation.JsonView;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;

import java.security.Principal;
import java.util.List;


@AllArgsConstructor
@Controller
public class CarController {
    private CarDAO carDAO;
    private CarService carService;

    @GetMapping("/cars")
    @JsonView(value = Views.Level3.class)
    public ResponseEntity<List<Car>> getCars() {
        return carService.getAllCars();
    }

    @GetMapping("managers/cars")
    @JsonView(value = Views.Level1.class)
    public ResponseEntity<List<Car>> getAllCars() {
        return carService.getAllCars();
    }

    @GetMapping("/cars/withSpecifications/{producerId}/{modelId}/{power}")
    @JsonView(value = Views.Level1.class)
    public ResponseEntity<List<Car>> getCar(@PathVariable Integer modelId, @PathVariable Integer producerId, @PathVariable int power) {
        String producer = getProducerById(producerId).getBody();
        String model = getModelByIdByProducerId(producerId, modelId).getBody();

        return carService.findAllWithSpecifications(
                CarSpecifications.byModel(model)
                        .and(CarSpecifications.byProducer(producer))
                        .and(CarSpecifications.byPower(power)));
    }

    @GetMapping("/cars/{id}")
    @JsonView(value = Views.Level1.class)
    public ResponseEntity<Car> getCars(@PathVariable int id) {
        return carService.getCar(id);
    }

    @JsonView(value = Views.Level3.class)
    @GetMapping("/producers/all")
    public ResponseEntity<List<Producer>> getAllProducers() {
        return  carService.getAllProducers();
    }
    @JsonView(value = Views.Level3.class)
    @GetMapping("/producer/{id}")
    public ResponseEntity<String> getProducerById(@PathVariable Integer id) {
        return  carService.getProducerById(id);
    }
    @JsonView(value = Views.Level3.class)
    @GetMapping("/producer/{producerId}/model/{modelId}")
    public ResponseEntity<String> getModelByIdByProducerId(@PathVariable Integer producerId, @PathVariable Integer modelId) {
        return carService.getModelByIdByProducerId(producerId, modelId);
    }
    @JsonView(value = Views.Level3.class)
    @GetMapping("/producers/{producerId}/models/all")
    public ResponseEntity<List<Model>> getAllModels(@PathVariable Integer producerId) {
        return  carService.getAllModels(producerId);
    }
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/cars")
    public ResponseEntity<String> save(@RequestBody  @Valid CarDTO carDTO, Principal principal) {
       return carService.save(carDTO, principal);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/cars/{id}")
    public  ResponseEntity<String>  deleteCarById(@PathVariable int id, Principal principal) {
       return carService.deleteCarById(id, principal);
    }


    @PatchMapping("/cars/{id}")
    public ResponseEntity<String> updateCar(@PathVariable int id, @RequestBody CarDTO carDTO, Principal principal) {
        return carService.updateCar(id, carDTO, principal);
    }

    @GetMapping("/cars/power/{value}")
    @JsonView(value = Views.Level2.class)
    public ResponseEntity<List<Car>> getCarsByPower(@PathVariable int value) {
        return carService.getCarsByPower(value);

    }

    @GetMapping("/cars/producer/{id}")
    @JsonView(value = Views.Level2.class)
    public ResponseEntity<List<Car>> getCarsByProducer(@PathVariable Integer id) {
        return carService.getCarsByProducer(id);
    }


    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/cars/saveWithPhotos")
    @JsonView(value = Views.Level1.class)
    public ResponseEntity<String> saveWithPhotos(@RequestParam int producerId, @RequestParam  int modelId, @RequestParam int power,
                              @RequestParam MultipartFile[] photos,    @RequestParam int year, @RequestParam String color, @RequestParam int mileage, @RequestParam int numberDoors, @RequestParam int numberSeats, Principal principal)
            throws IOException {
       return carService.saveWithPhotos(producerId ,modelId,  power, photos, year, color,mileage, numberDoors, numberSeats, principal);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/cars/{id}/savePhoto")
    @JsonView(value = Views.Level1.class)
    public void savePhotoToCarId(@PathVariable Integer id, @RequestParam MultipartFile[] photos)
            throws IOException {
        carService.savePhotoToCarId(id, photos);
    }

    @JsonView(value = Views.Level3.class)
    @GetMapping("/regions/all")
    public ResponseEntity<List<Region>> getAllRegions() {
        return  carService.getAllRegions();
    }
    @JsonView(value = Views.Level3.class)
    @GetMapping("/region/{id}")
    public ResponseEntity<Region> getRegionById(@PathVariable Integer id) {
        return  carService.getRegionById(id);
    }
}

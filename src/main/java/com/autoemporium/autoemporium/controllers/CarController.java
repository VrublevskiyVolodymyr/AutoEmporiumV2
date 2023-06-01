package com.autoemporium.autoemporium.controllers;



import com.autoemporium.autoemporium.dao.CarDAO;
import com.autoemporium.autoemporium.models.Car;
import com.autoemporium.autoemporium.models.Model;
import com.autoemporium.autoemporium.models.Producer;
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

import java.util.List;


@AllArgsConstructor
@Controller
public class CarController {
    private CarDAO carDAO;
    private CarService carService;

    @GetMapping("/cars")
    @JsonView(value = Views.Level3.class)
    public ResponseEntity<List<Car>> getAllCars() {
        return carService.getAllCars();
    }

    @GetMapping("/cars/withSpecifications/{model}/{producer}/{power}")
    @JsonView(value = Views.Level1.class)
    public ResponseEntity<List<Car>> getCar(@PathVariable String model, @PathVariable String producer, @PathVariable int power) {
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

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/cars")
    public void save(@RequestBody @Valid Car car) {
        carService.save(car);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/cars/{id}")
    public void deleteCar(@PathVariable int id) {
        carService.deleteCar(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/cars/model/{model}")
    public void deleteCarByModel(@PathVariable String model) {
        carService.deleteCarByModel(model);
    }

    @PatchMapping("/cars/{id}")
    public ResponseEntity<Car> updateCar(@PathVariable int id, @RequestBody Car car) {
        return carService.updateCar(id, car);
    }

    @GetMapping("/cars/power/{value}")
    @JsonView(value = Views.Level2.class)
    public ResponseEntity<List<Car>> getCarsByPower(@PathVariable int value) {
        return carService.getCarsByPower(value);

//        return carDAO.findByPower(value);
    }

    @GetMapping("/cars/producer/{value}")
    @JsonView(value = Views.Level2.class)
    public ResponseEntity<List<Car>> getCarByProducer(@PathVariable String value) {
        return carService.getCarByProducer(value);
    }

    @GetMapping("/cars/producers")
    @JsonView(value = Views.Level2.class)
    public ResponseEntity<List<String>> getAllByProducers() {
        return carService.getAllProducers();
    }

    @GetMapping("/cars/models/producer/{value}")
    @JsonView(value = Views.Level2.class)
    public ResponseEntity<List<String>> getAllModelsByProducer(@PathVariable String value) {
        return carService.getAllModelsByProducer(value);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/cars/saveWithPhoto")
    @JsonView(value = Views.Level1.class)
    public void saveWithPhoto(@RequestParam Model model, @RequestParam Producer producer, @RequestParam int power,
                              @RequestParam MultipartFile photo, @RequestParam int year, @RequestParam String color, @RequestParam int numberDoors, @RequestParam int numberSeats)
            throws IOException {
        carService.saveWithPhoto(model, producer, power, photo, year, color, numberDoors, numberSeats);
    }
}

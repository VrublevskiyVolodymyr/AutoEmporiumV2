package com.autoemporium.autoemporium.services;

import com.autoemporium.autoemporium.dao.CarDAO;
import com.autoemporium.autoemporium.models.Car;
import com.autoemporium.autoemporium.models.Model;
import com.autoemporium.autoemporium.models.Producer;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.util.List;


@Service
@AllArgsConstructor
public class CarService {
    private CarDAO carDAO;

    public void save(Car car) {
        if (car == null) {
            throw new RuntimeException();
        }
        carDAO.save(car);
    }
    public ResponseEntity<List<Car>> getAllCars() {
        Sort by = Sort.by(Sort.Order.desc("id"));
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

    public void deleteCar(int id) {
        if (id > 0) {
            carDAO.deleteById(id);
        }
    }

    public void deleteCarByModel(String model) {
        carDAO.deleteCarByModel(model);
    }

    public ResponseEntity<Car> updateCar(int id, Car car) {
        Car c = carDAO.findById(id).get();
        c.setModel(car.getModel());
        c.setProducer(car.getProducer());
        c.setPower(car.getPower());
        c.setYear(car.getYear());
        c.setColor(car.getColor());
        c.setNumberDoors(car.getNumberDoors());
        c.setNumberSeats(car.getNumberSeats());
        carDAO.save(c);
        return new ResponseEntity<>(c,HttpStatus.OK);
    }

    public ResponseEntity<List<Car>> getCarsByPower(@PathVariable int value) {
        return new ResponseEntity<>(carDAO.getCarsByPower(value), HttpStatus.OK);

//        return carDAO.findByPower(value);
    }

    public ResponseEntity<List<Car>> getCarByProducer(@PathVariable String value) {
        return new ResponseEntity<>(carDAO.findByProducer(value), HttpStatus.OK);
    }
    public ResponseEntity<List<String>> getAllProducers() {
        return new ResponseEntity<>(carDAO.findDistinctProducers(), HttpStatus.OK);
    }

    public ResponseEntity<List<String>> getAllModelsByProducer(@PathVariable String value) {
        return new ResponseEntity<>(carDAO.findDistinctModelsByProducer(value), HttpStatus.OK);
    }

    public void saveWithPhoto(
            @RequestParam Model model,
            @RequestParam Producer producer,
            @RequestParam int power,
            @RequestParam MultipartFile photo,
            @RequestParam int year,
            @RequestParam String color,
            @RequestParam int numberDoors,
            @RequestParam int numberSeats

    ) throws IOException {Car car = new Car(model, producer, power, year, color, numberDoors, numberSeats);
        String originalFilename = photo.getOriginalFilename();
        car.setPhoto("/photo/" + originalFilename);
        String path = System.getProperty("user.home") + File.separator + "images" + File.separator + originalFilename;
        File file = new File(path);
        photo.transferTo(file);
        carDAO.save(car);
    }
}

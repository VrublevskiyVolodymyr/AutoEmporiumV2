package com.autoemporium.autoemporium.controllers;

import com.autoemporium.autoemporium.models.Advertisement;
import com.autoemporium.autoemporium.models.AdvertisementDTO;
import com.autoemporium.autoemporium.models.Currency;
import com.autoemporium.autoemporium.services.AdvertisementService;
import com.autoemporium.autoemporium.views.Views;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;


@RestController
public class AdvertisementController {

    private final AdvertisementService service;

    public AdvertisementController(@Qualifier("advertisementServiceImpl1") AdvertisementService service) {
        this.service = service;
    }


    @GetMapping("/advertisements/all")
    @JsonView(value = Views.Level2.class)
    public ResponseEntity<List<Advertisement>> getAllAvailableAdvertisement() {
        return service.getAllAvailableAdvertisement();
    }


    @GetMapping("/admin/advertisements/all")
    @JsonView(value = Views.Level1.class)
    public ResponseEntity<List<Advertisement>> getAllAdvertisement() {
        return service.getAllAdvertisement();
    }


    @GetMapping("advertisement/autoDealer/{autoDealerId}")
    @JsonView(value = Views.Level2.class)
    public ResponseEntity<List<Advertisement>>getAllAdvByAutoDealers(@PathVariable int autoDealerId) {
        return service.getAllAdvByAutoDealers(autoDealerId);
    }

    @GetMapping("/advertisement/{id}")
    @JsonView(value = Views.Level2.class )
    public ResponseEntity<Advertisement> getAvailableAdvertisementById(@PathVariable Integer id,Principal principal) {
        return service.getAvailableAdvertisementById(id, principal);
    }

    @GetMapping("admin/advertisement/{id}")
    @JsonView(value = Views.Level1.class )
    public ResponseEntity<Advertisement> getAdvertisementById(@PathVariable Integer id,Principal principal) {
        return service.getAdvertisementById(id, principal);
    }

    @PostMapping("/advertisements/saveByCarId")
    public ResponseEntity<String> createByCarId (@RequestBody AdvertisementDTO advertisementDTO, Principal principal) {
        return  service.saveByCarId(advertisementDTO, principal);
    }
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/advertisements/saveWithCar")
    public ResponseEntity<String> saveWithCar (@RequestBody AdvertisementDTO advertisementDTO, Principal principal) {
        return  service.saveWithCar(advertisementDTO, principal);
    }

    @PostMapping("/advertisements/saveWithCarWithPhoto")
    public ResponseEntity<String> saveWithCarWithPhoto (Principal principal, @RequestParam int modelId, @RequestParam int producerId, @RequestParam int power,
                                                        @RequestParam MultipartFile[] photos, @RequestParam int year, @RequestParam String color,
                                                        @RequestParam int mileage, @RequestParam int numberDoors, @RequestParam int numberSeats, @RequestParam  String title,
                                                        @RequestParam  String description, @RequestParam BigDecimal price, @RequestParam Currency currency, @RequestParam int regionId )
            throws IOException {
        return  service.saveWithCarWithPhoto(principal,modelId, producerId, power, photos, year, color,mileage, numberDoors, numberSeats,title,description,price,currency,regionId );
    }

    @PatchMapping("/advertisements/{id}")
    public  ResponseEntity<String>  edit(@PathVariable int id, @RequestBody Advertisement advertisement, Principal principal) {
        return   service.edit(id, advertisement,principal);
    }


    @DeleteMapping("/advertisement/{id}")
    public  ResponseEntity<String>  deleteAdvById(@PathVariable int id, Principal principal) {
        return service.deleteAdvertisementById(id, principal);
    }

    @JsonView(value = Views.Level2.class)
    @GetMapping("/advertisements/getAdvByCarPower/{value}")
    public ResponseEntity<List<Advertisement>> getAdvByCarPower(@PathVariable int value, Principal principal) {
    return service.getAdvByCarPower(value,principal);
    }

    @JsonView(value = Views.Level2.class)
    @GetMapping("/advertisements/getAdvByCarProducer/{producerId}")
    public ResponseEntity<List<Advertisement>> getAdvByCarProducer(@PathVariable Integer producerId, Principal principal) {
        return service.getAdvByCarProducer(producerId,principal);
    }

    @JsonView(value = Views.Level2.class)
    @GetMapping("/advertisements/getAdvByModel/{producerId}/{modelId}")
    public ResponseEntity<List<Advertisement>> getAdvByCarProducerAndCarModel(@PathVariable Integer producerId, @PathVariable Integer modelId, Principal principal) {
        return service.getAdvByCarProducerAndCarModel(producerId, modelId,principal);
    }
}

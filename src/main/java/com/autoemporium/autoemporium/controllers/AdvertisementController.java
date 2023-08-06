package com.autoemporium.autoemporium.controllers;

import com.autoemporium.autoemporium.dao.AdvertisementDAO;
import com.autoemporium.autoemporium.models.Advertisement;
import com.autoemporium.autoemporium.models.AdvertisementDTO;
import com.autoemporium.autoemporium.models.Car;
import com.autoemporium.autoemporium.models.Currency;
import com.autoemporium.autoemporium.services.AdvertisementService;
import com.autoemporium.autoemporium.views.Views;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@AllArgsConstructor
@Controller
public class AdvertisementController {

    private AdvertisementService service;

//    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/advertisements/all")
//    @JsonView(value = Views.Level1.class)
    public ResponseEntity<List<Advertisement>> getAllAdvertisement() {
        return service.getAllAdvertisement();
    }
    @GetMapping("/advertisement/{id}")
    @JsonView(value = Views.Level2.class )
    public ResponseEntity<Advertisement> getAdvertisementById(@PathVariable Integer id,Principal principal) {
        return service.getAdvertisementById(id, principal);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/advertisements/saveByCarId")
    public ResponseEntity<String> createByCarId (@RequestBody AdvertisementDTO advertisementDTO, Principal principal) {
        return  service.saveByCarId(advertisementDTO, principal);
    }
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/advertisements/saveWithCar")
    public ResponseEntity<String> saveWithCar (@RequestBody AdvertisementDTO advertisementDTO, Principal principal) {
        return  service.saveWithCar(advertisementDTO, principal);
    }
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/advertisements/saveWithCarWithPhoto")
    public ResponseEntity<String> saveWithCarWithPhoto (Principal principal, @RequestParam int modelId, @RequestParam int producerId, @RequestParam int power,
                                                        @RequestParam MultipartFile[] photos, @RequestParam int year, @RequestParam String color,
                                                        @RequestParam int mileage, @RequestParam int numberDoors, @RequestParam int numberSeats, @RequestParam  String title,
                                                        @RequestParam  String description, @RequestParam BigDecimal price, @RequestParam Currency currency, @RequestParam int regionId )
            throws IOException {
        return  service.saveWithCarWithPhoto(principal,modelId, producerId, power, photos, year, color,mileage, numberDoors, numberSeats,title,description,price,currency,regionId );
    }
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/advertisements/{id}")
    public  ResponseEntity<String>  edit(@PathVariable int id, @RequestBody Advertisement advertisement, Principal principal) {
        return   service.edit(id, advertisement,principal);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/advertisement/{id}")
    public  ResponseEntity<String>  deleteAdvById(@PathVariable int id, Principal principal) {
        return service.deleteAdvertisementById(id, principal);
    }
}

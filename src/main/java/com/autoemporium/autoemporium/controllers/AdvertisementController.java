package com.autoemporium.autoemporium.controllers;

import com.autoemporium.autoemporium.dao.AdvertisementDAO;
import com.autoemporium.autoemporium.models.Advertisement;
import com.autoemporium.autoemporium.models.Car;
import com.autoemporium.autoemporium.services.AdvertisementService;
import com.autoemporium.autoemporium.views.Views;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
public class AdvertisementController {

    @Autowired
    private AdvertisementService service;

//    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/advertisements/all")
//    @JsonView(value = Views.Level1.class)
    public ResponseEntity<List<Advertisement>> getAllAdvertisement() {
        return service.getAllAdvertisement();
    }
    @GetMapping("/advertisement/{id}")
//    @JsonView(value = Views.Level1.class)
    public ResponseEntity<Advertisement> getAdvertisementById(@PathVariable Integer id) {
        return service.getAdvertisementById(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/advertisements")
    public ResponseEntity<String> create(@RequestBody Advertisement advertisement, Principal principal) {
        return  service.save(advertisement, principal);
    }
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/advertisements/{id}")
    public  ResponseEntity<String>  edit(@PathVariable int id, @RequestBody Advertisement advertisement) {
        return   service.edit(id, advertisement);
    }
}

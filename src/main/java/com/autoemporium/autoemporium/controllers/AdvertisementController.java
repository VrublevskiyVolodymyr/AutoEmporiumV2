package com.autoemporium.autoemporium.controllers;

import com.autoemporium.autoemporium.dao.AdvertisementDAO;
import com.autoemporium.autoemporium.models.Advertisement;
import com.autoemporium.autoemporium.services.AdvertisementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
public class AdvertisementController {

    @Autowired
    private AdvertisementService service;

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

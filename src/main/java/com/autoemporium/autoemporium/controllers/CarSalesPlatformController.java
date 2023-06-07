package com.autoemporium.autoemporium.controllers;

import com.autoemporium.autoemporium.dao.ModelDAO;
import com.autoemporium.autoemporium.dao.ProducerDAO;
import com.autoemporium.autoemporium.models.Client;
import com.autoemporium.autoemporium.models.ClientDTO;
import com.autoemporium.autoemporium.models.Model;
import com.autoemporium.autoemporium.models.Producer;
import com.autoemporium.autoemporium.services.CarSalePlatformService;
import com.autoemporium.autoemporium.views.Views;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@AllArgsConstructor
@RestController
public class CarSalesPlatformController {
    CarSalePlatformService carSalePlatformService;


    @PostMapping("/producer/{producer}")
    public ResponseEntity<String> notifyMissingProducer(@PathVariable String producer, Principal principal) {
            return carSalePlatformService.notifyMissingProducer(producer, principal);
    }

    @PostMapping("/model/{producerId}/{model}")
    public ResponseEntity<String> notifyMissingModel(@PathVariable String model, @PathVariable Integer producerId,Principal principal) {
      return carSalePlatformService.notifyMissingModel(model,producerId,principal);
    }
        @ResponseStatus(HttpStatus.OK)
    @PostMapping("/producers/save")
    public ResponseEntity<String> saveProducers(@RequestBody List<Producer> producers) {
       return carSalePlatformService.saveProducers(producers);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/producers/save/{producer}")
    public ResponseEntity<String> saveProducer(@PathVariable String producer ) {
       return carSalePlatformService.saveProducer(producer);
    }
    @JsonView(value = Views.Level3.class)
    @GetMapping("/producers/all")
    public ResponseEntity<List<Producer>> getAllProducers() {
        return  carSalePlatformService.getAllProducers();
    }
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/producers/{producerId}/models/save")
    public ResponseEntity<String> saveModels(@RequestBody List<Model> models, @PathVariable Integer producerId) {
       return carSalePlatformService.saveModels(models,producerId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/producers/{producerId}/model/save")
    public ResponseEntity<String> saveModel(@RequestBody Model model, @PathVariable Integer producerId) {
       return carSalePlatformService.saveModel(model,producerId);
    }
    @JsonView(value = Views.Level3.class)
    @GetMapping("/producers/{producerId}/models/all")
    public ResponseEntity<List<Model>> getAllModels(@PathVariable Integer producerId) {
        return  carSalePlatformService.getAllModels(producerId);
    }
}

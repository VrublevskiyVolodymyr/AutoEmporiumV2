package com.autoemporium.autoemporium.controllers;

import com.autoemporium.autoemporium.models.Client;
import com.autoemporium.autoemporium.models.ClientDTO;
import com.autoemporium.autoemporium.models.Model;
import com.autoemporium.autoemporium.models.Producer;
import com.autoemporium.autoemporium.services.CarSalePlatformService;
import com.autoemporium.autoemporium.views.Views;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
public class CarSalesPlatformController {
    CarSalePlatformService carSalePlatformService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/producers/save")
    public void saveProducers(@RequestBody List<Producer> producers) {
        carSalePlatformService.saveProducers(producers);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/producers/save/{producer}")
    public void saveProducer(@RequestBody Producer producer ) {
        carSalePlatformService.saveProducer(producer);
    }
    @JsonView(value = Views.Level3.class)
    @GetMapping("/producers/all")
    public ResponseEntity<List<Producer>> getAllProducers() {
        return  carSalePlatformService.getAllProducers();
    }
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/producers/{producerId}/models/save")
    public void saveModels(@RequestBody List<Model> models, @PathVariable Integer producerId) {
        carSalePlatformService.saveModels(models,producerId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/producers/{producerId}/model/save")
    public void saveModel(@RequestBody Model model, @PathVariable Integer producerId) {
        carSalePlatformService.saveModel(model,producerId);
    }
    @JsonView(value = Views.Level3.class)
    @GetMapping("/producers/{producerId}/models/all")
    public ResponseEntity<List<Model>> getAllModels(@PathVariable Integer producerId) {
        return  carSalePlatformService.getAllModels(producerId);
    }

}

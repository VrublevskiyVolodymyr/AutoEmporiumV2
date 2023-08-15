package com.autoemporium.autoemporium.controllers;

import com.autoemporium.autoemporium.models.cars.Model;
import com.autoemporium.autoemporium.models.cars.Producer;
import com.autoemporium.autoemporium.models.cars.Region;
import com.autoemporium.autoemporium.services.userService.ManagerService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@RestController
public class ManagerController {
    ManagerService managerService;

    public ManagerController(@Qualifier("managerServiceImpl1") ManagerService managerService) {
        this.managerService = managerService;
    }

    @PostMapping("/notify/producer/{producer}")
    public ResponseEntity<String> notifyMissingProducer(@PathVariable String producer, Principal principal) {
            return managerService.notifyMissingProducer(producer, principal);
    }

    @PostMapping("/notify/model/{producerId}/{model}")
    public ResponseEntity<String> notifyMissingModel(@PathVariable String model, @PathVariable Integer producerId,Principal principal) {
      return managerService.notifyMissingModel(model,producerId,principal);
    }

    @PostMapping("/producers/save")
    public ResponseEntity<String> saveProducers(@RequestBody List<Producer> producers) {
       return managerService.saveProducers(producers);
    }

    @PostMapping("/producers/save/{producer}")
    public ResponseEntity<String> saveProducer(@PathVariable String producer) {
       return managerService.saveProducer(producer);
    }

    @PostMapping("/producers/{producerId}/models/save")
    public ResponseEntity<String> saveModels(@RequestBody List<Model> models, @PathVariable Integer producerId) {
       return managerService.saveModels(models,producerId);
    }

    @PostMapping("/producers/{producerId}/model/save")
    public ResponseEntity<String> saveModel(@RequestBody Model model, @PathVariable Integer producerId) {
       return managerService.saveModel(model,producerId);
    }


    @PostMapping("/regions/save")
    public ResponseEntity<String> saveRegions(@RequestBody List<Region> regions) {
        return managerService.saveRegions(regions);
    }

    @PostMapping("/regions/save/{region}")
    public ResponseEntity<String> saveRegion(@PathVariable String region ) {
        return managerService.saveRegion(region);
    }
}

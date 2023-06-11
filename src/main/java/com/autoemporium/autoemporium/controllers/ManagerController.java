package com.autoemporium.autoemporium.controllers;

import com.autoemporium.autoemporium.models.Model;
import com.autoemporium.autoemporium.models.Producer;
import com.autoemporium.autoemporium.services.ManagerService;
import com.autoemporium.autoemporium.views.Views;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@AllArgsConstructor
@RestController
public class ManagerController {
    ManagerService managerService;


    @PostMapping("/producer/{producer}")
    public ResponseEntity<String> notifyMissingProducer(@PathVariable String producer, Principal principal) {
            return managerService.notifyMissingProducer(producer, principal);
    }

    @PostMapping("/model/{producerId}/{model}")
    public ResponseEntity<String> notifyMissingModel(@PathVariable String model, @PathVariable Integer producerId,Principal principal) {
      return managerService.notifyMissingModel(model,producerId,principal);
    }
        @ResponseStatus(HttpStatus.OK)
    @PostMapping("/producers/save")
    public ResponseEntity<String> saveProducers(@RequestBody List<Producer> producers) {
       return managerService.saveProducers(producers);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/producers/save/{producer}")
    public ResponseEntity<String> saveProducer(@PathVariable String producer ) {
       return managerService.saveProducer(producer);
    }
    @JsonView(value = Views.Level3.class)
    @GetMapping("/producers/all")
    public ResponseEntity<List<Producer>> getAllProducers() {
        return  managerService.getAllProducers();
    }
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/producers/{producerId}/models/save")
    public ResponseEntity<String> saveModels(@RequestBody List<Model> models, @PathVariable Integer producerId) {
       return managerService.saveModels(models,producerId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/producers/{producerId}/model/save")
    public ResponseEntity<String> saveModel(@RequestBody Model model, @PathVariable Integer producerId) {
       return managerService.saveModel(model,producerId);
    }
    @JsonView(value = Views.Level3.class)
    @GetMapping("/producers/{producerId}/models/all")
    public ResponseEntity<List<Model>> getAllModels(@PathVariable Integer producerId) {
        return  managerService.getAllModels(producerId);
    }
}

package com.autoemporium.autoemporium.services.userService;

import com.autoemporium.autoemporium.models.cars.Model;
import com.autoemporium.autoemporium.models.cars.Producer;
import com.autoemporium.autoemporium.models.cars.Region;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;
import java.util.List;

public interface ManagerService {
    public ResponseEntity<String> notifyMissingProducer(@PathVariable String producer, Principal principal);
    public ResponseEntity<String> notifyMissingModel(@PathVariable String model, @PathVariable Integer producerId,Principal principal);
    public ResponseEntity<String> saveProducers(@RequestBody List<Producer> producers);
    public ResponseEntity<String> saveProducer(@PathVariable String producer);
    public ResponseEntity<String> saveModels(@RequestBody List<Model> models, @PathVariable Integer producerId);
    public ResponseEntity<String> saveModel(@RequestBody Model model, @PathVariable Integer producerId);
    public ResponseEntity<String> saveRegions(@RequestBody List<Region> regions);
    public ResponseEntity<String> saveRegion(@PathVariable String region );
}

package com.autoemporium.autoemporium.services.userService;

import com.autoemporium.autoemporium.models.cars.Model;
import com.autoemporium.autoemporium.models.cars.Producer;
import com.autoemporium.autoemporium.models.cars.Region;
import com.autoemporium.autoemporium.services.userService.ManagerService;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.List;

public class ManagerServiceImpl2 implements ManagerService {
    @Override
    public ResponseEntity<String> notifyMissingProducer(String producer, Principal principal) {
        return null;
    }

    @Override
    public ResponseEntity<String> notifyMissingModel(String model, Integer producerId, Principal principal) {
        return null;
    }

    @Override
    public ResponseEntity<String> saveProducers(List<Producer> producers) {
        return null;
    }

    @Override
    public ResponseEntity<String> saveProducer(String producer) {
        return null;
    }

    @Override
    public ResponseEntity<String> saveModels(List<Model> models, Integer producerId) {
        return null;
    }

    @Override
    public ResponseEntity<String> saveModel(Model model, Integer producerId) {
        return null;
    }

    @Override
    public ResponseEntity<String> saveRegions(List<Region> regions) {
        return null;
    }

    @Override
    public ResponseEntity<String> saveRegion(String region) {
        return null;
    }
}

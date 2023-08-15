package com.autoemporium.autoemporium.controllers;

import com.autoemporium.autoemporium.services.infoService.InfoAdvService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class InfoAdvController {
    private final InfoAdvService infoAdvService;

    public InfoAdvController(@Qualifier("infoAdvServiceImpl1") InfoAdvService infoAdvService) {
        this.infoAdvService = infoAdvService;
    }

    @GetMapping("/views/advertisement/{id}/daily-count")
    public ResponseEntity<Integer> getDailyViewCount(@PathVariable int id, Principal principal) {
        return infoAdvService.getDailyViewCountForAdvertisement(id,principal);
    }

    @GetMapping("/views/advertisement/{id}/weekly-count")
    public ResponseEntity<Integer> getWeeklyViewCount(@PathVariable int id, Principal principal) {
        return infoAdvService.getWeeklyViewCountForAdvertisement(id,principal);
    }

    @GetMapping("/views/advertisement/{id}/monthly-count")
    public ResponseEntity<Integer> getMonthlyViewCount(@PathVariable int id, Principal principal) {
        return infoAdvService.getMonthlyViewCountForAdvertisement(id,principal);
    }

    @GetMapping("/views/advertisement/{id}/all-count")
    public ResponseEntity<Integer> getAllViewCountForAdvertisement(@PathVariable int id, Principal principal) {
        return infoAdvService.getAllViewCountForAdvertisement(id,principal);
    }

    @GetMapping("/average-price/by-region/{regionId}/producer/{producerId}/model/{modelId}/currency/{currency}")
    public ResponseEntity<Double> getAveragePriceByRegion(@PathVariable Integer regionId, @PathVariable Integer producerId, @PathVariable Integer modelId, @PathVariable String currency, Principal principal) {
        return infoAdvService.getAveragePriceByRegion(regionId,producerId, modelId,currency,principal);
    }

    @GetMapping("/average-price/for-ukraine/producer/{producerId}/model/{modelId}/currency/{currency}")
    public ResponseEntity<Double> getAveragePriceForUkraine(@PathVariable Integer producerId, @PathVariable Integer modelId, @PathVariable String currency, Principal principal) {
        return infoAdvService.getAveragePriceForUkraine(producerId, modelId,currency,principal);
    }
}

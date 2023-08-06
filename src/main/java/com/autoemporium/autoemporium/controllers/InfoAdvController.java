package com.autoemporium.autoemporium.controllers;

import com.autoemporium.autoemporium.services.InfoAdvService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class InfoAdvController {
    private InfoAdvService infoAdvService;


    @GetMapping("/views/advertisement/{id}/daily-count")
    public ResponseEntity<Integer> getDailyViewCount(@PathVariable int id) {
        return infoAdvService.getDailyViewCountForAdvertisement(id);
    }

    @GetMapping("/views/advertisement/{id}/weekly-count")
    public ResponseEntity<Integer> getWeeklyViewCount(@PathVariable int id) {
        return infoAdvService.getWeeklyViewCountForAdvertisement(id);
    }

    @GetMapping("/views/advertisement/{id}/monthly-count")
    public ResponseEntity<Integer> getMonthlyViewCount(@PathVariable int id) {
        return infoAdvService.getMonthlyViewCountForAdvertisement(id);
    }

    @GetMapping("/views/advertisement/{id}/all-count")
    public ResponseEntity<Integer> getAllViewCountForAdvertisement(@PathVariable int id) {
        return infoAdvService.getAllViewCountForAdvertisement(id);
    }

    @GetMapping("/average-price/by-region/{regionId}/producer/{producerId}/model/{modelId}/currency/{currency}")
    public ResponseEntity<Double> getAveragePriceByRegion(@PathVariable Integer regionId, @PathVariable Integer producerId, @PathVariable Integer modelId, @PathVariable String currency) {
        return infoAdvService.getAveragePriceByRegion(regionId,producerId, modelId,currency);
    }

    @GetMapping("/average-price/for-ukraine/producer/{producerId}/model/{modelId}/currency/{currency}")
    public ResponseEntity<Double> getAveragePriceForUkraine(@PathVariable Integer producerId, @PathVariable Integer modelId, @PathVariable String currency) {
        return infoAdvService.getAveragePriceForUkraine(producerId, modelId,currency);
    }
}

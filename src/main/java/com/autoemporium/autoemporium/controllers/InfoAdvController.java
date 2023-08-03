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
}

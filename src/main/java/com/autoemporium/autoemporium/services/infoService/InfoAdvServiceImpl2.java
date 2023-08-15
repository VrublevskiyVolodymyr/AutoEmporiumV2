package com.autoemporium.autoemporium.services.infoService;

import com.autoemporium.autoemporium.services.infoService.InfoAdvService;
import org.springframework.http.ResponseEntity;

import java.security.Principal;

public class InfoAdvServiceImpl2 implements InfoAdvService {
    @Override
    public ResponseEntity<Integer> getDailyViewCountForAdvertisement(int advertisementId, Principal principal) {
        return null;
    }

    @Override
    public ResponseEntity<Integer> getWeeklyViewCountForAdvertisement(int advertisementId, Principal principal) {
        return null;
    }

    @Override
    public ResponseEntity<Integer> getMonthlyViewCountForAdvertisement(int advertisementId, Principal principal) {
        return null;
    }

    @Override
    public ResponseEntity<Integer> getAllViewCountForAdvertisement(int id, Principal principal) {
        return null;
    }

    @Override
    public ResponseEntity<Double> getAveragePriceByRegion(Integer producerId, Integer regionId, Integer modelId, String currency, Principal principal) {
        return null;
    }

    @Override
    public ResponseEntity<Double> getAveragePriceForUkraine(Integer producerId, Integer modelId, String currency, Principal principal) {
        return null;
    }
}

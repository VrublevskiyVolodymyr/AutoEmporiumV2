package com.autoemporium.autoemporium.services.infoService;

import org.springframework.http.ResponseEntity;

import java.security.Principal;

public interface InfoAdvService {
    public ResponseEntity<Integer> getDailyViewCountForAdvertisement(int advertisementId, Principal principal);

    public ResponseEntity<Integer> getWeeklyViewCountForAdvertisement(int advertisementId, Principal principal);

    public ResponseEntity<Integer> getMonthlyViewCountForAdvertisement(int advertisementId, Principal principal);

    public ResponseEntity<Integer> getAllViewCountForAdvertisement(int id, Principal principal);

    public ResponseEntity<Double> getAveragePriceByRegion(Integer producerId, Integer regionId, Integer modelId, String currency, Principal principal);

    public ResponseEntity<Double> getAveragePriceForUkraine(Integer producerId, Integer modelId, String currency, Principal principal);


}

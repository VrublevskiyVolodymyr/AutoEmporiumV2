package com.autoemporium.autoemporium.services;

import com.autoemporium.autoemporium.models.Advertisement;
import com.autoemporium.autoemporium.models.AdvertisementDTO;
import com.autoemporium.autoemporium.models.Currency;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

public class AdvertisementServiceImpl2 implements AdvertisementService{
    @Override
    public ResponseEntity<List<Advertisement>> getAllAvailableAdvertisement() {
        return null;
    }

    @Override
    public ResponseEntity<List<Advertisement>> getAllAdvertisement() {
        return null;
    }

    @Override
    public ResponseEntity<Advertisement> getAvailableAdvertisementById(Integer id, Principal principal) {
        return null;
    }

    @Override
    public ResponseEntity<Advertisement> getAdvertisementById(Integer id, Principal principal) {
        return null;
    }

    @Override
    public ResponseEntity<String> saveByCarId(AdvertisementDTO advertisementDTO, Principal principal) {
        return null;
    }

    @Override
    public ResponseEntity<String> saveWithCar(AdvertisementDTO advertisementDTO, Principal principal) {
        return null;
    }

    @Override
    public ResponseEntity<String> saveWithCarWithPhoto(Principal principal, int modelId, int producerId, int power, MultipartFile[] photos, int year, String color, int mileage, int numberDoors, int numberSeats, String title, String description, BigDecimal price, Currency currency, int regionId) throws IOException {
        return null;
    }

    @Override
    public ResponseEntity<String> edit(int id, Advertisement advertisement, Principal principal) {
        return null;
    }

    @Override
    public ResponseEntity<String> deleteAdvertisementById(int id, Principal principal) {
        return null;
    }

    @Override
    public ResponseEntity<List<Advertisement>> getAdvByCarPower(int value, Principal principal) {
        return null;
    }

    @Override
    public ResponseEntity<List<Advertisement>> getAdvByCarProducer(Integer producerId, Principal principal) {
        return null;
    }

    @Override
    public ResponseEntity<List<Advertisement>> getAdvByCarProducerAndCarModel(Integer producerId, Integer modelId, Principal principal) {
        return null;
    }

    @Override
    public ResponseEntity<List<Advertisement>> getAllAdvByAutoDealers(int id) {
        return null;
    }

}

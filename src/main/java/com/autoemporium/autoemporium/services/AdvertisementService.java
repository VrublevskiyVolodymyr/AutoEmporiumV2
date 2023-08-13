package com.autoemporium.autoemporium.services;

import com.autoemporium.autoemporium.models.Advertisement;
import com.autoemporium.autoemporium.models.AdvertisementDTO;
import com.autoemporium.autoemporium.models.Currency;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

public interface AdvertisementService {
    public ResponseEntity<List<Advertisement>> getAllAvailableAdvertisement();

    public ResponseEntity<List<Advertisement>> getAllAdvertisement();

    public ResponseEntity<Advertisement> getAvailableAdvertisementById(@PathVariable Integer id, Principal principal);

    public ResponseEntity<Advertisement> getAdvertisementById(@PathVariable Integer id, Principal principal);

    public ResponseEntity<String> saveByCarId(@RequestBody AdvertisementDTO advertisementDTO, Principal principal);

    public ResponseEntity<String> saveWithCar(@RequestBody AdvertisementDTO advertisementDTO, Principal principal);

    public ResponseEntity<String> saveWithCarWithPhoto(Principal principal, @RequestParam int modelId, @RequestParam int producerId, @RequestParam int power,
                                                       @RequestParam MultipartFile[] photos, @RequestParam int year, @RequestParam String color,
                                                       @RequestParam int mileage, @RequestParam int numberDoors, @RequestParam int numberSeats, @RequestParam String title,
                                                       @RequestParam String description, @RequestParam BigDecimal price, @RequestParam Currency currency, @RequestParam int regionId) throws IOException;

    public ResponseEntity<String> edit(@PathVariable int id, @RequestBody Advertisement advertisement, Principal principal);

    public ResponseEntity<String> deleteAdvertisementById(@PathVariable int id, Principal principal);

    public ResponseEntity<List<Advertisement>> getAdvByCarPower(int value, Principal principal);

    public ResponseEntity<List<Advertisement>> getAdvByCarProducer(Integer producerId, Principal principal);

    public ResponseEntity<List<Advertisement>> getAdvByCarProducerAndCarModel(Integer producerId, Integer modelId, Principal principal);

    public ResponseEntity<List<Advertisement>> getAllAdvByAutoDealers(int id);
}

package com.autoemporium.autoemporium.services;

import com.autoemporium.autoemporium.models.Car;
import com.autoemporium.autoemporium.models.users.AccountType;
import com.autoemporium.autoemporium.dao.AdvertisementDAO;
import com.autoemporium.autoemporium.dao.SellerDAO;
import com.autoemporium.autoemporium.models.Advertisement;
import com.autoemporium.autoemporium.models.users.Seller;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class AdvertisementService {
    private final List<String> bannedWords = Arrays.asList("bad", "offensive", "word");
    @Autowired
    private SellerDAO sellerDAO;
    @Autowired
    private UserService userService;

    @Autowired
    private AdvertisementDAO advertisementDAO;


    MailService mailService;


    public ResponseEntity<String> save(Advertisement advertisement, Principal principal) {
        String username = principal.getName();
        Seller seller = sellerDAO.findSellerByUsername(username);
        if (seller.getAccountType() == AccountType.BASIC && seller.getCountOfAds() >= 1) {
            return new ResponseEntity<>("You can only post one advertisement with a basic account.", HttpStatus.FORBIDDEN);
        } else if (containsBannedWords(advertisement.getTitle()) || containsBannedWords(advertisement.getDescription())) {
            advertisement.setStatus(false);
            advertisement.setCreatedAt(LocalDateTime.now());
            advertisement.setCreatedBy(seller);
            seller.setCountOfAds(seller.getCountOfAds() + 1);
            Advertisement savedAdvertisement = advertisementDAO.save(advertisement);
            int id = savedAdvertisement.getId();

            return new ResponseEntity<>("You massage contains banned words. Edit massage, id = " + id, HttpStatus.FORBIDDEN);
        } else {
            advertisement.setCreatedAt(LocalDateTime.now());
            advertisement.setStatus(true);
            advertisement.setCreatedBy(seller);
            seller.setCountOfAds(seller.getCountOfAds() + 1);
            advertisementDAO.save(advertisement);
            return new ResponseEntity<>(HttpStatus.OK);
        }

    }

    public ResponseEntity<String> edit(int id, Advertisement advertisement) {
        Advertisement existingAdvertisement = advertisementDAO.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid advertisement id"));

        if (existingAdvertisement.getEditCount() >= 3) {
            return new ResponseEntity<>("You massage id = " + id + " is banned, contact the administrator", HttpStatus.FORBIDDEN);
        }

        if (containsBannedWords(advertisement.getTitle()) || containsBannedWords(advertisement.getDescription())) {
            existingAdvertisement.setEditCount(existingAdvertisement.getEditCount() + 1);
            existingAdvertisement.setStatus(false);
            existingAdvertisement.setEditedAt(LocalDateTime.now());
            advertisementDAO.save(existingAdvertisement);
            int count = 3 - existingAdvertisement.getEditCount();

            if (count==0) {
                String managerEmail = userService.getManagerEmail();
                String subject = "Notification from Autoemporium: AdvertisementService";
                String body = "advertisement " + existingAdvertisement.toString() +  "is banned";
                mailService.sendEmail(managerEmail, subject, body);
                return new ResponseEntity<>("You massage id = " + id + " is banned, contact the administrator", HttpStatus.FORBIDDEN);
            }

            return new ResponseEntity<>("You massage contains banned words. Edit massage, id = " + id + " You have " + count + " tries", HttpStatus.FORBIDDEN);
        }

        existingAdvertisement.setTitle(advertisement.getTitle());
        existingAdvertisement.setDescription(advertisement.getDescription());
        existingAdvertisement.setEditCount(existingAdvertisement.getEditCount() + 1);
        existingAdvertisement.setStatus(true);
        existingAdvertisement.setEditedAt(LocalDateTime.now());
        advertisementDAO.save(existingAdvertisement);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private boolean containsBannedWords(String text) {
        return bannedWords.stream().anyMatch(text::contains);
    }

    public ResponseEntity<List<Advertisement>> getAllAdvertisement() {

        List<Advertisement> all = advertisementDAO.findAll();

        return new ResponseEntity<>(all,HttpStatus.OK);
    }

    public ResponseEntity<Advertisement> getAdvertisementById(Integer id) {
        Advertisement advertisement = advertisementDAO.findById(id).orElse(null);
        return new ResponseEntity<>(advertisement,HttpStatus.OK);
    }
}

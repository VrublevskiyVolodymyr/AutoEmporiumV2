package com.autoemporium.autoemporium.services;

import com.autoemporium.autoemporium.dao.*;
import com.autoemporium.autoemporium.models.Advertisement;
import com.autoemporium.autoemporium.models.AdvertisementView;
import com.autoemporium.autoemporium.models.users.AccountType;
import com.autoemporium.autoemporium.models.users.Seller;
import com.autoemporium.autoemporium.models.users.User;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@Service
public class InfoAdvService {

    private AdvertisementViewDAO advertisementViewDAO;
    private AdvertisementDAO advertisementDAO;
    private RegionDAO regionDAO;
    private ModelDAO modelDAO;
    private ProducerDAO producerDAO;
    private SellerDAO sellerDAO;
    private UserDAO userDAO;

    public ResponseEntity<Integer> getDailyViewCountForAdvertisement(int advertisementId, Principal principal) {
       if(isPremiumAccount(principal)){
           HttpHeaders httpHeaders = new HttpHeaders();
           httpHeaders.add("Error", "buy Premium account");
           return new ResponseEntity<>(httpHeaders,HttpStatus.FORBIDDEN);
       }

        LocalDate today = LocalDate.now();
        List<AdvertisementView> viewsToday = advertisementViewDAO.findByViewDateBetweenAndAdvertisementId(today, today, advertisementId);
        int dailyViewCount = viewsToday.size();

        if (dailyViewCount == 0) {
            Advertisement advertisement = advertisementDAO.findById(advertisementId).orElse(null);
            if (advertisement == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<>(dailyViewCount, HttpStatus.OK);
    }

    public ResponseEntity<Integer> getWeeklyViewCountForAdvertisement(int advertisementId, Principal principal) {
        if(isPremiumAccount(principal)){
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Error", "buy Premium account");
            return new ResponseEntity<>(httpHeaders,HttpStatus.FORBIDDEN);
        }

        LocalDate oneWeekAgo = LocalDate.now().minusDays(7);
        List<AdvertisementView> viewsLastWeek = advertisementViewDAO.findByViewDateAfterAndAdvertisementId(oneWeekAgo, advertisementId);
        int weeklyViewCount = viewsLastWeek.size();

        if (weeklyViewCount == 0) {
            Advertisement advertisement = advertisementDAO.findById(advertisementId).orElse(null);
            if (advertisement == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }

        return new ResponseEntity<>(weeklyViewCount, HttpStatus.OK);
    }

    public ResponseEntity<Integer> getMonthlyViewCountForAdvertisement(int advertisementId, Principal principal) {
        if(isPremiumAccount(principal)){
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Error", "buy Premium account");
            return new ResponseEntity<>(httpHeaders,HttpStatus.FORBIDDEN);
        }

        LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);
        List<AdvertisementView> viewsLastMonth = advertisementViewDAO.findByViewDateAfterAndAdvertisementId(oneMonthAgo, advertisementId);
        System.out.println(viewsLastMonth);
        int monthlyViewCount = viewsLastMonth.size();

        if (monthlyViewCount == 0) {
            Advertisement advertisement = advertisementDAO.findById(advertisementId).orElse(null);
            if (advertisement == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }

        return new ResponseEntity<>(monthlyViewCount, HttpStatus.OK);
    }

    public ResponseEntity<Integer> getAllViewCountForAdvertisement(int id, Principal principal) {
        if(isPremiumAccount(principal)){
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Error", "buy Premium account");
            return new ResponseEntity<>(httpHeaders,HttpStatus.FORBIDDEN);
        }

        Advertisement advertisement = advertisementDAO.findById(id).get();
        if (advertisement == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        int allViewCount = advertisement.getViews();
        return new ResponseEntity<>(allViewCount, HttpStatus.OK);
    }

    public ResponseEntity<Double> getAveragePriceByRegion(Integer producerId, Integer regionId, Integer modelId, String currency, Principal principal) {
        if(isPremiumAccount(principal)){
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Error", "buy Premium account");
            return new ResponseEntity<>(httpHeaders,HttpStatus.FORBIDDEN);
        }

        String region = regionDAO.findById(regionId).get().getRegion();
        String producer = producerDAO.findById(producerId).get().getProducer();
        String model = modelDAO.findById(modelId).get().getModel();
        Double averagePrice = advertisementDAO.findAveragePriceByRegionAndProducerAndModel(region, producer, model, currency);
        if (averagePrice == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(averagePrice, HttpStatus.OK);
    }

    public ResponseEntity<Double> getAveragePriceForUkraine(Integer producerId, Integer modelId, String currency, Principal principal) {
        if(isPremiumAccount(principal)){
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Error", "buy Premium account");
            return new ResponseEntity<>(httpHeaders,HttpStatus.FORBIDDEN);
        }

        String producer = producerDAO.findById(producerId).get().getProducer();
        String model = modelDAO.findById(modelId).get().getModel();
        Double averagePrice = advertisementDAO.findAveragePriceForUkraineByProducerAndModel(producer, model, currency);
        if (averagePrice == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(averagePrice, HttpStatus.OK);
    }

    public boolean isPremiumAccount(Principal principal){
        String username = principal.getName();
        Seller seller = sellerDAO.findSellerByUsername(username);
        User user = userDAO.findByUsername(username);
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        boolean isSellerWithPremium =  seller != null && seller.getAccountType() == AccountType.PREMIUM;
        boolean isAdmin = false;

        for (GrantedAuthority authority : authorities) {
            String authorityName = authority.getAuthority();
            if ("ADMIN".equals(authorityName) || "MANAGER".equals(authorityName)) {
                isAdmin = true;
                break;
            }
        }
        return !isSellerWithPremium && !isAdmin;
    }
}

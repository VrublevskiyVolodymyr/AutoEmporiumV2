package com.autoemporium.autoemporium.services;

import com.autoemporium.autoemporium.dao.AdvertisementViewDAO;
import com.autoemporium.autoemporium.models.AdvertisementView;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Service
public class InfoAdvService {

    private AdvertisementViewDAO advertisementViewDAO;

    public ResponseEntity<Integer> getDailyViewCountForAdvertisement(int advertisementId) {
        LocalDate today = LocalDate.now();
        List<AdvertisementView> viewsToday = advertisementViewDAO.findByViewDateBetween(today, today);
        int dailyViewCount = viewsToday.size();

        return new ResponseEntity<>(dailyViewCount, HttpStatus.OK);
    }

    public ResponseEntity<Integer> getWeeklyViewCountForAdvertisement(int advertisementId) {
        LocalDate oneWeekAgo = LocalDate.now().minusDays(7);
        List<AdvertisementView> viewsLastWeek = advertisementViewDAO.findByViewDateAfter(oneWeekAgo);
        int weeklyViewCount = viewsLastWeek.size();

        return new ResponseEntity<>(weeklyViewCount, HttpStatus.OK);
    }

    public ResponseEntity<Integer> getMonthlyViewCountForAdvertisement(int advertisementId) {
        LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);
        List<AdvertisementView> viewsLastMonth = advertisementViewDAO.findByViewDateAfter(oneMonthAgo);
        int monthlyViewCount = viewsLastMonth.size();

        return new ResponseEntity<>(monthlyViewCount, HttpStatus.OK);
    }
}

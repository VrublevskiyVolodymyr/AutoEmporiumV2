package com.autoemporium.autoemporium.dao;

import com.autoemporium.autoemporium.models.AdvertisementView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AdvertisementViewDAO extends JpaRepository<AdvertisementView, Integer> {
    List<AdvertisementView> findByViewDateBetweenAndAdvertisementId(LocalDate startDate, LocalDate endDate, int advertisementId);
    List<AdvertisementView> findByViewDateAfterAndAdvertisementId(LocalDate startDate, int advertisementId);
}

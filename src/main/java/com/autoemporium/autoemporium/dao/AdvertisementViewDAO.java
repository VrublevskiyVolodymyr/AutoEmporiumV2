package com.autoemporium.autoemporium.dao;

import com.autoemporium.autoemporium.models.AdvertisementView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AdvertisementViewDAO extends JpaRepository<AdvertisementView, Integer> {
    List<AdvertisementView> findByViewDateBetween(LocalDate startDate, LocalDate endDate);
    List<AdvertisementView> findByViewDateAfter(LocalDate startDate);

}

package com.autoemporium.autoemporium.dao;

import com.autoemporium.autoemporium.models.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdvertisementDAO extends JpaRepository<Advertisement, Integer> {
}

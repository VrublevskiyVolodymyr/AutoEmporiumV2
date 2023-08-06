package com.autoemporium.autoemporium.dao;

import com.autoemporium.autoemporium.models.Region;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionDAO extends JpaRepository<Region, Integer> {
}

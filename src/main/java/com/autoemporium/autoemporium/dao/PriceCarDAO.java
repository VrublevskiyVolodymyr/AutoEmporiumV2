package com.autoemporium.autoemporium.dao;

import com.autoemporium.autoemporium.models.cars.PriceCar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceCarDAO extends JpaRepository<PriceCar,Integer> {
}

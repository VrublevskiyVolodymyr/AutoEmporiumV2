package com.autoemporium.autoemporium.dao;

import com.autoemporium.autoemporium.models.cars.Model;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModelDAO extends JpaRepository<Model, Integer> {
}

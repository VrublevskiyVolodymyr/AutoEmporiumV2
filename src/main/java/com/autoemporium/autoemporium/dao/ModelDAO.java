package com.autoemporium.autoemporium.dao;

import com.autoemporium.autoemporium.models.Advertisement;
import com.autoemporium.autoemporium.models.Model;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModelDAO extends JpaRepository<Model, Integer> {
}

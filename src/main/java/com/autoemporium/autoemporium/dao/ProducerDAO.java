package com.autoemporium.autoemporium.dao;

import com.autoemporium.autoemporium.models.Advertisement;
import com.autoemporium.autoemporium.models.Producer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProducerDAO extends JpaRepository<Producer, Integer> {
}

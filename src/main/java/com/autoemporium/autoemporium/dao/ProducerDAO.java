package com.autoemporium.autoemporium.dao;

import com.autoemporium.autoemporium.models.cars.Producer;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProducerDAO extends JpaRepository<Producer, Integer> {

}

package com.autoemporium.autoemporium.dao;

import com.autoemporium.autoemporium.models.Producer;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ProducerDAO extends JpaRepository<Producer, Integer> {

}

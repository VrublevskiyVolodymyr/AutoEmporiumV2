package com.autoemporium.autoemporium.dao;

import com.autoemporium.autoemporium.models.Car;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface CarDAO extends JpaRepository<Car, Integer>, JpaSpecificationExecutor<Car> {
    @Query("select c from Car c where c.power=:power")
    List<Car> getCarsByPower(@Param("power") int power);

//    @Query("select distinct c.producer from Car c")
//    List<String> findDistinctProducers();

//    @Query("select distinct c.model from Car c where  c.producer = :producer")
//    List<String> findDistinctModelsByProducer(@Param("producer") String producer);
    List<Car> findByProducer(String value);

    List<Car> findByPower(int power);

    @Modifying
    @Transactional
    void deleteCarByModel(String model);

}

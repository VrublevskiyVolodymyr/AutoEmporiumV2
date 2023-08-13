package com.autoemporium.autoemporium.dao;

import com.autoemporium.autoemporium.models.Advertisement;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AdvertisementDAO extends JpaRepository<Advertisement, Integer> {
    List<Advertisement> findByRegion(String region);

    List<Advertisement> findByStatus(boolean status);

    Advertisement findByIdAndStatus(Integer id,boolean status);

    Advertisement findFirstByTitleAndDescription(String title, String description);

    Advertisement findByCarId(int carId);

    List<Advertisement> findByStatusAndCar_Producer(Boolean status,String producer);

    List<Advertisement> findByStatusAndCar_ProducerAndCar_Model(Boolean status,String producer, String model);

    List<Advertisement> findByStatusAndCar_Power(Boolean status,Integer power);

    List<Advertisement> findAllByCreatedByDealerId(int id);

    @Query("SELECT AVG(CASE WHEN :currency = 'UAH' THEN a.priceCar.UAH WHEN :currency = 'USD' THEN a.priceCar.USD WHEN :currency = 'EUR' THEN a.priceCar.EUR END) FROM Advertisement a WHERE a.region = :region AND a.car.producer = :producer AND a.car.model = :model")
    Double findAveragePriceByRegionAndProducerAndModel(String region, String producer,String model, String currency);

    @Query("SELECT AVG(CASE WHEN :currency = 'UAH' THEN a.priceCar.UAH WHEN :currency = 'USD' THEN a.priceCar.USD WHEN :currency = 'EUR' THEN a.priceCar.EUR END) FROM Advertisement a WHERE a.car.producer = :producer AND a.car.model = :model")
    Double findAveragePriceForUkraineByProducerAndModel(String producer,String model, String currency);


}

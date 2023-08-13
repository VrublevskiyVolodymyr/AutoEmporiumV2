package com.autoemporium.autoemporium.dao;

import com.autoemporium.autoemporium.models.AutoDealer;
import com.autoemporium.autoemporium.models.users.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutoDealerDAO extends JpaRepository<AutoDealer, Integer> {
    AutoDealer findByNameOrLocation(String name, String location);

    boolean existsByNameOrLocation(String name, String location);

    AutoDealer findAutoDealerBySalesIsContaining(Seller seller);
}

package com.autoemporium.autoemporium.dao;

import com.autoemporium.autoemporium.models.users.Buyer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BuyerDAO extends JpaRepository<Buyer, Integer> {
    @Query("select b from Buyer b where b.user.username=:username")
    Buyer findBuyerByUsername(@Param("username") String username);
}

package com.autoemporium.autoemporium.dao;

import com.autoemporium.autoemporium.models.PremiumPurchase;

import com.autoemporium.autoemporium.models.users.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PremiumPurchaseDAO extends JpaRepository<PremiumPurchase,Integer> {
    PremiumPurchase findBySeller(Seller seller);
    PremiumPurchase findPremiumPurchaseBySellerId(Integer sellerId);
}


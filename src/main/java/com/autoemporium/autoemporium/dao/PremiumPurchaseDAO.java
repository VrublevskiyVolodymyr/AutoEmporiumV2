package com.autoemporium.autoemporium.dao;

import com.autoemporium.autoemporium.models.financial.PremiumPurchase;

import com.autoemporium.autoemporium.models.users.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PremiumPurchaseDAO extends JpaRepository<PremiumPurchase,Integer> {
    PremiumPurchase findBySeller(Seller seller);
    PremiumPurchase findPremiumPurchaseBySellerId(Integer sellerId);
}


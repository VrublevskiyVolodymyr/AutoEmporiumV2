package com.autoemporium.autoemporium.dao;

import com.autoemporium.autoemporium.models.financial.CurrencyPrivatbank;

import org.springframework.data.jpa.repository.JpaRepository;


public interface CurrencyPrivatbankDAO extends JpaRepository<CurrencyPrivatbank, Long> {
    CurrencyPrivatbank findByName(String name);
}

package com.autoemporium.autoemporium.services.financialServices;

import com.autoemporium.autoemporium.models.advertisement.Advertisement;
import com.autoemporium.autoemporium.models.financial.CurrencyPrivatbank;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class CurrencyServiceImpl2 implements CurrencyService{
    @Override
    public ResponseEntity<List<CurrencyPrivatbank>> getCurrencies() {
        return null;
    }

    @Override
    public ResponseEntity<CurrencyPrivatbank> getCurrencyByName(String name) {
        return null;
    }

    @Override
    public void updateCurrencyRates() throws JsonProcessingException {

    }

    @Override
    public void convertCurrency(Advertisement advertisement) {

    }

    @Override
    public void setDefaultPriceCar(Advertisement advertisement) {

    }
}

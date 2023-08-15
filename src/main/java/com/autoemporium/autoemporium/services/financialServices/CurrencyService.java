package com.autoemporium.autoemporium.services.financialServices;

import com.autoemporium.autoemporium.models.advertisement.Advertisement;
import com.autoemporium.autoemporium.models.financial.CurrencyPrivatbank;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface CurrencyService {
    public ResponseEntity<List<CurrencyPrivatbank>> getCurrencies();
    public ResponseEntity<CurrencyPrivatbank> getCurrencyByName(@PathVariable String name);
    public   void updateCurrencyRates() throws JsonProcessingException;
    public void convertCurrency(Advertisement advertisement);
    public void setDefaultPriceCar(Advertisement advertisement);
}

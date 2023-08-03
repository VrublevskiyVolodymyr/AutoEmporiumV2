package com.autoemporium.autoemporium.services;

import com.autoemporium.autoemporium.dao.AdvertisementDAO;
import com.autoemporium.autoemporium.dao.CurrencyPrivatbankDAO;
import com.autoemporium.autoemporium.models.Advertisement;
import com.autoemporium.autoemporium.models.AdvertisementDTO;
import com.autoemporium.autoemporium.models.Currency;
import com.autoemporium.autoemporium.models.CurrencyPrivatbank;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;



@Service
public class CurrencyService {
    private final CurrencyPrivatbankDAO currencyDAO;
    private AdvertisementDAO advertisementDAO;
    private AdvertisementService advertisementService;

    public CurrencyService(CurrencyPrivatbankDAO currencyDAO, AdvertisementDAO advertisementDAO, @Lazy AdvertisementService advertisementService) {
        this.currencyDAO = currencyDAO;
        this.advertisementDAO = advertisementDAO;
        this.advertisementService = advertisementService;
    }

    public void updateCurrencyRates() throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.privatbank.ua/p24api/pubinfo?json&exchange&coursid=5";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        List<CurrencyPrivatbank> currencies = objectMapper.readValue(response.getBody(),
                new TypeReference<List<CurrencyPrivatbank>>() {
                });
        for (CurrencyPrivatbank currency : currencies) {
            CurrencyPrivatbank existingCurrency = currencyDAO.findByName(currency.getName());
            if (existingCurrency != null) {
                existingCurrency.setBuyRate(currency.getBuyRate());
                existingCurrency.setSellRate(currency.getSellRate());
            } else {
                currencyDAO.save(currency);
            }
        }
        List<Advertisement> advertisements = advertisementDAO.findAll();
        for (Advertisement advertisement : advertisements) {
            if (advertisement.getCurrency() == Currency.UAH) {
                advertisementService.convertCurrency(advertisement);
            } else {
                advertisementService.setDefaultPriceCar(advertisement);
            }
        }
    }

    public BigDecimal getCurrencyRate(String currencyName) {
        CurrencyPrivatbank currency = currencyDAO.findByName(currencyName);
        if (currency != null) {
            return currency.getSellRate();
        }
        return null;
    }
}

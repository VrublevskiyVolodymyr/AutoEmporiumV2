//package com.autoemporium.autoemporium.services;
//
//
//import com.autoemporium.autoemporium.dao.CurrencyPrivatbankDAO;
//import com.autoemporium.autoemporium.models.CurrencyPrivatbank;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//
//import lombok.NoArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import java.math.BigDecimal;
//import java.util.List;
//
//
//@Service
//public class CurrencyServiceImpl extends CurrencyService {
//    @Autowired
//    private CurrencyPrivatbankDAO currencyDAO;
//
//    public CurrencyServiceImpl(CurrencyPrivatbankDAO currencyDAO) {
//        super(currencyDAO);
//    }
//
//    @Override
//    public void updateCurrencyRates() throws JsonProcessingException {
//        RestTemplate restTemplate = new RestTemplate();
//        String url = "https://api.privatbank.ua/p24api/pubinfo?json&exchange&coursid=5";
//        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
//        ObjectMapper objectMapper = new ObjectMapper();
//        List<CurrencyPrivatbank> currencies = objectMapper.readValue(response.getBody(),
//                new TypeReference<List<CurrencyPrivatbank>>() {
//                });
//        for (CurrencyPrivatbank currency : currencies) {
//            CurrencyPrivatbank existingCurrency = currencyDAO.findByName(currency.getName());
//            if (existingCurrency != null) {
//                existingCurrency.setBuyRate(currency.getBuyRate());
//                existingCurrency.setSellRate(currency.getSellRate());
//            } else {
//                currencyDAO.save(currency);
//            }
//        }
//    }
//    public BigDecimal getCurrencyRate(String currencyName) {
//        CurrencyPrivatbank currency = currencyDAO.findByName(currencyName);
//        if (currency != null) {
//            return currency.getSellRate();
//        }
//        return null;
//    }
//
//}

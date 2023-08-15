package com.autoemporium.autoemporium.services.financialServices;

import com.autoemporium.autoemporium.dao.AdvertisementDAO;
import com.autoemporium.autoemporium.dao.CurrencyPrivatbankDAO;
import com.autoemporium.autoemporium.models.advertisement.Advertisement;
import com.autoemporium.autoemporium.models.financial.Currency;
import com.autoemporium.autoemporium.models.financial.CurrencyPrivatbank;
import com.autoemporium.autoemporium.models.cars.PriceCar;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;


@AllArgsConstructor
@Service
public class CurrencyServiceImpl1 implements CurrencyService {
    private final CurrencyPrivatbankDAO currencyDAO;
    private final AdvertisementDAO advertisementDAO;

    public ResponseEntity<List<CurrencyPrivatbank>> getCurrencies() {
        return new ResponseEntity<>(currencyDAO.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<CurrencyPrivatbank> getCurrencyByName(@PathVariable String name) {
        return new ResponseEntity<>(currencyDAO.findByName(name), HttpStatus.OK);
    }

    public   void updateCurrencyRates() throws JsonProcessingException {
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
                this.convertCurrency(advertisement);
            } else {
                this.setDefaultPriceCar(advertisement);
            }
        }
    }

    public   BigDecimal getCurrencyRate(String currencyName) {
        CurrencyPrivatbank currency = currencyDAO.findByName(currencyName);
        if (currency != null) {
            return currency.getSellRate();
        }
        return null;
    }

    public void convertCurrency(Advertisement advertisement) {
        BigDecimal sellRateUSD = this.getCurrencyRate(Currency.USD.name());
        BigDecimal sellRateEUR = this.getCurrencyRate(Currency.EUR.name());

        if (sellRateUSD != null && sellRateEUR != null) {
            BigDecimal priceUSD = advertisement.getPrice().divide(sellRateUSD, 2, RoundingMode.HALF_UP);
            BigDecimal priceEUR = advertisement.getPrice().divide(sellRateEUR, 2, RoundingMode.HALF_UP);

            PriceCar priceCar = new PriceCar();
            priceCar.setUAH(advertisement.getPrice());
            priceCar.setUSD(priceUSD);
            priceCar.setEUR(priceEUR);

            advertisement.setPriceCar(priceCar);
        } else {
            throw new RuntimeException("The sell rate for the specified currency is not available. Please choose a different currency.");
        }
    }

    public void setDefaultPriceCar(Advertisement advertisement) {
        PriceCar priceCar = new PriceCar();
        priceCar.setUAH(BigDecimal.ZERO);
        priceCar.setUSD(BigDecimal.ZERO);
        priceCar.setEUR(BigDecimal.ZERO);

        advertisement.setPriceCar(priceCar);

        BigDecimal price = advertisement.getPrice();
        Currency currency = advertisement.getCurrency();
        BigDecimal sellRate = this.getCurrencyRate(currency.name());

        if (sellRate != null) {
            BigDecimal priceUAH = price.multiply(sellRate);
            BigDecimal priceUSD = priceUAH.divide(this.getCurrencyRate(Currency.USD.name()), 2, RoundingMode.HALF_UP);
            BigDecimal priceEUR = priceUAH.divide(this.getCurrencyRate(Currency.EUR.name()), 2, RoundingMode.HALF_UP);

            advertisement.getPriceCar().setUAH(priceUAH);
            advertisement.getPriceCar().setUSD(priceUSD);
            advertisement.getPriceCar().setEUR(priceEUR);
        } else {
            throw new RuntimeException("The sell rate for the specified currency is not available. Please choose a different currency.");
        }
    }
}

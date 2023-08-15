package com.autoemporium.autoemporium.controllers;

import com.autoemporium.autoemporium.models.financial.CurrencyPrivatbank;
import com.autoemporium.autoemporium.services.financialServices.CurrencyService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


@Controller
public class CurrencyPrivatbankController {
    private final CurrencyService currencyService;

    public CurrencyPrivatbankController(@Qualifier("currencyServiceImpl1") CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping("/currency")
    public ResponseEntity<List<CurrencyPrivatbank>> getCurrencies() {
        return currencyService.getCurrencies();
    }

    @GetMapping("/currency/{name}")
    public ResponseEntity<CurrencyPrivatbank> getCurrencyByName(@PathVariable String name) {
        return currencyService.getCurrencyByName(name);
    }
}

package com.autoemporium.autoemporium.controllers;

import com.autoemporium.autoemporium.dao.CurrencyPrivatbankDAO;
import com.autoemporium.autoemporium.models.CurrencyPrivatbank;
import com.autoemporium.autoemporium.services.CurrencyService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@AllArgsConstructor
@Controller
public class CurrencyPrivatbankController {
    private CurrencyService currencyService;


    @GetMapping("/currency")
    public ResponseEntity<List<CurrencyPrivatbank>> getCurrencies() {
        return currencyService.getCurrencies();
    }

    @GetMapping("/currency/{name}")
    public ResponseEntity<CurrencyPrivatbank> getCurrencyByName(@PathVariable String name) {
        return currencyService.getCurrencyByName(name);
    }
}

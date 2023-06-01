package com.autoemporium.autoemporium.controllers;

import com.autoemporium.autoemporium.dao.CurrencyPrivatbankDAO;
import com.autoemporium.autoemporium.models.CurrencyPrivatbank;
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
    private CurrencyPrivatbankDAO currencyDAO;

    @GetMapping("/currency")
    public ResponseEntity<List<CurrencyPrivatbank>> getCurrencies() {
        return new ResponseEntity<>(currencyDAO.findAll(), HttpStatus.OK);
    }

    @GetMapping("/currency/{name}")
    public ResponseEntity<CurrencyPrivatbank> getCurrencyByName(@PathVariable String name) {
        return new ResponseEntity<>(currencyDAO.findByName(name), HttpStatus.OK);
    }
}

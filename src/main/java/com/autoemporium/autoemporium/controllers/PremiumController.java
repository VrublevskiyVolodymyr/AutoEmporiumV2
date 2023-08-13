package com.autoemporium.autoemporium.controllers;


import com.autoemporium.autoemporium.services.financialServices.LiqPayService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class PremiumController {
    private final LiqPayService liqPayService;

    public PremiumController(@Qualifier("liqPayServiceImpl1") LiqPayService liqPayService) {
        this.liqPayService = liqPayService;
    }


    @GetMapping("/buy-premium")
    public ResponseEntity<String> buyPremiumAccount( Principal principal) {
        return liqPayService.createPayment(principal);
    }


    @GetMapping("/confirmation-of-payment/orderId/{orderId}")
    public ResponseEntity<String> getPaymentStatus(@PathVariable String orderId, Principal principal) throws JsonProcessingException {
        return liqPayService.getPayPaymentStatus(orderId,principal);
    }

    @GetMapping("/orderId/sellerId/{sellerId}")
    public ResponseEntity<String> getOrderIdBySellerId(@PathVariable Integer sellerId) {
        return liqPayService.getOrderIdBySellerId(sellerId);
    }
}

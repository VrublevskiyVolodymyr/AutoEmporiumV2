package com.autoemporium.autoemporium.services.financialServices;

import org.springframework.http.ResponseEntity;

import java.security.Principal;

public interface LiqPayService {
    public ResponseEntity<String> createPayment(Principal principal);
    public ResponseEntity<String> getPayPaymentStatus(String orderId, Principal principal);
    public ResponseEntity<String> getOrderIdBySellerId(Integer sellerId);
}

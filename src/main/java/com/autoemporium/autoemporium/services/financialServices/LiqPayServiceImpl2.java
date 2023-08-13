package com.autoemporium.autoemporium.services.financialServices;


import com.autoemporium.autoemporium.services.financialServices.LiqPayService;
import org.springframework.http.ResponseEntity;

import java.security.Principal;

public class LiqPayServiceImpl2 implements LiqPayService {
    @Override
    public ResponseEntity<String> createPayment(Principal principal) {
        return null;
    }

    @Override
    public ResponseEntity<String> getPayPaymentStatus(String orderId, Principal principal) {
        return null;
    }

    @Override
    public ResponseEntity<String> getOrderIdBySellerId(Integer sellerId) {
        return null;
    }
}

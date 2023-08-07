package com.autoemporium.autoemporium.services;

import com.autoemporium.autoemporium.models.PremiumPurchaseRequest;
import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
//@ConfigurationProperties(prefix = "liqpay")
public class LiqPayService {
    @org.springframework.beans.factory.annotation.Value(value = "${liqpay.publicKey}")
    private String publicKey;
    @org.springframework.beans.factory.annotation.Value(value = "${liqpay.privateKey}")
    private String privateKey;

    private final String apiUrl = "https://www.liqpay.ua/api/3/checkout";

    public ResponseEntity<String> createPayment(PremiumPurchaseRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(publicKey, privateKey);

        // Формування тіла запиту з даними платежу
        String requestBody = "{ \"version\": \"3\", \"public_key\": \"" + publicKey + "\", " +
                "\"action\": \"pay\", \"amount\": " + request.getAmount() + ", \"currency\": \"" + request.getCurrency() + "\", " +
                "\"description\": \"" + request.getDescription() + "\", \"order_id\": \"" + request.getOrder_id() + "\" }";

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);

        return response;
    }
}

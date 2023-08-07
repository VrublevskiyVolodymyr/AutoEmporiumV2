package com.autoemporium.autoemporium.controllers;

import com.autoemporium.autoemporium.models.PremiumPurchaseRequest;
import com.autoemporium.autoemporium.services.LiqPayService;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class PremiumController {
    private final LiqPayService liqPayService;


    @PostMapping("/buy-premium")
    public ResponseEntity<String> buyPremiumAccount(@RequestBody PremiumPurchaseRequest premiumPurchaseRequest) {
        // Виклик сервісного методу для створення платежу
        ResponseEntity<String> response = liqPayService.createPayment(premiumPurchaseRequest);

        // Обробка відповіді від LiqPay API
        if (response.getStatusCode() == HttpStatus.OK) {
            // Успішна відповідь, обробити результат платежу
            String responseBody = response.getBody();
            // Тут ви можете обробити результат платежу і зробити необхідні дії відповідно до статусу платежу

            // Приклад виводу статусу платежу
            System.out.println("Статус платежу: " + responseBody);

            // Додатково можна здійснити редірект користувача на сторінку успішної оплати або інші дії

            return response;
        } else {
            // Помилкова відповідь, обробити помилку
            // Тут ви можете обробити помилку, якщо з'явилася проблема з LiqPay API

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

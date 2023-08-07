package com.autoemporium.autoemporium.models;

import com.autoemporium.autoemporium.views.Views;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class PremiumPurchaseRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String order_id;
    private Double amount;
    private String currency;
    private String description;
}

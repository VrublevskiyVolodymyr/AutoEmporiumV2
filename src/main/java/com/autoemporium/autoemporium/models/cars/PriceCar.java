package com.autoemporium.autoemporium.models.cars;

import com.autoemporium.autoemporium.views.Views;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
//@ToString
@Entity
public class PriceCar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(value = Views.Level1.class)
    private int id;
    @JsonView(value = {Views.Level1.class, Views.Level2.class, Views.Level3.class })
    @JsonProperty("UAH")
    private BigDecimal UAH;
    @JsonView(value = {Views.Level1.class, Views.Level2.class, Views.Level3.class })
    @JsonProperty("USD")
    private BigDecimal USD;
    @JsonView(value = {Views.Level1.class, Views.Level2.class, Views.Level3.class })
    @JsonProperty("EUR")
    private BigDecimal EUR;
}

package com.autoemporium.autoemporium.models.financial;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Entity
@Table(name = "currencyprivatbank")
public class CurrencyPrivatbank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JsonProperty("ccy")
    private String name;

    @JsonProperty("base_ccy")
    private String baseCurrency;

    @JsonProperty("buy")
    private BigDecimal buyRate;

    @JsonProperty("sale")
    private BigDecimal sellRate;
}

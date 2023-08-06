package com.autoemporium.autoemporium.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class AdvertisementDTO {
    private String title;
    private String description;
    private BigDecimal price;
    private Currency currency;
    private int carId;
    private int regionId;
    private CarDTO carDTO;

    public AdvertisementDTO(String title, String description, BigDecimal price, Currency currency, int regionId, int carId) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.currency = currency;
        this.regionId = regionId;
        this.carId = carId;
    }

    public AdvertisementDTO(String title, String description, BigDecimal price, Currency currency, int regionId, CarDTO carDTO) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.currency = currency;
        this.regionId = regionId;
        this.carDTO = carDTO;
    }
}

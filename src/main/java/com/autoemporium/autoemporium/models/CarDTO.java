package com.autoemporium.autoemporium.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CarDTO {
    private int id;
    private String model;
    private Producer producer;
    private int power;
    private int year;
    private String color;
    private int numberDoors;
    private int numberSeats;
}

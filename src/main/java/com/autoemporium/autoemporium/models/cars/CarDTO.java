package com.autoemporium.autoemporium.models.cars;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CarDTO {
    private int id;

    @Min(value = 1, message ="power cannot be less then 1")
    @Max(value = 1100, message = "power cannot be more then 100")
    private int modelId;

    @Min(value = 1, message ="producerId cannot be less then 1")
    @Max(value = 1100, message = "producerId cannot be more then 100")
    private int producerId;

    @Min(value = 1, message ="power cannot be less then 1")
    @Max(value = 1100, message = "power cannot be more then 1100")
    private int power;
    private int year;
    private int mileage;
    private String color;

    @Min(value = 2, message ="number of door cannot be less then 2")
    @Max(value = 5, message = "number of door cannot be more then 5")
    private int numberDoors;

    @Min(value = 1, message ="number of seats cannot be less then 1")
    @Max(value = 80, message = "number of seats cannot be more then 80")
    private int numberSeats;
    private List<String> photo;

    public CarDTO(int modelId, int producerId, int power, int year, int mileage, String color, int numberDoors, int numberSeats) {
        this.modelId = modelId;
        this.producerId = producerId;
        this.power = power;
        this.year = year;
        this.mileage = mileage;
        this.color = color;
        this.numberDoors = numberDoors;
        this.numberSeats = numberSeats;
    }
}

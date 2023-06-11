package com.autoemporium.autoemporium.models;

import com.autoemporium.autoemporium.views.Views;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity

public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(value = {Views.Level1.class})
    private int id;

    @JsonView(value = {Views.Level1.class,Views.Level2.class,Views.Level3.class})
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "car_model",
            joinColumns = @JoinColumn(name = "car_id"),
            inverseJoinColumns = @JoinColumn(name = "model_id")
    )
    private Model model;
//    @NotBlank(message = "producer cannot be empty")

    @JsonView(value = {Views.Level1.class,Views.Level2.class,Views.Level3.class})
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "car_producer",
            joinColumns = @JoinColumn(name = "car_id"),
            inverseJoinColumns = @JoinColumn(name = "producer_id")
    )
    private Producer producer;

    @Min(value = 1, message ="power cannot be less then 1")
    @Max(value = 1100, message = "power cannot be more then 1100")

    @JsonView(value = {Views.Level1.class,Views.Level2.class})
    private int power;

    @Min(value = 1900, message ="power cannot be less then 1")
    @Max(value = 2023, message = "power cannot be greater then current year")

    @JsonView(value = {Views.Level1.class,Views.Level2.class})
    private int year;

    @JsonView(value = {Views.Level1.class})
    private String color;

    @JsonView(value = {Views.Level1.class})
    private Integer mileage;

    @Min(value = 2, message ="number of door cannot be less then 2")
    @Max(value = 5, message = "number of door cannot be more then 5")

    @JsonView(value = {Views.Level1.class,Views.Level2.class})
    private int numberDoors;

    @Min(value = 1, message ="number of seats cannot be less then 1")
    @Max(value = 80, message = "number of seats cannot be more then 80")

    @JsonView(value = {Views.Level1.class,Views.Level2.class})
    private int numberSeats;


    @JsonView(value = {Views.Level1.class})
    private String photo;

    public Car(Model model, Producer producer, int power) {
        this.model = model;
        this.producer = producer;
        this.power = power;
    }

    public Car(Model  model, Producer producer, int power, int year, String color, int numberDoors, int numberSeats) {
        this.model = model;
        this.producer = producer;
        this.power = power;
        this.year = year;
        this.color = color;
        this.numberDoors = numberDoors;
        this.numberSeats= numberSeats;
    }
}

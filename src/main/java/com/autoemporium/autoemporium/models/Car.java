package com.autoemporium.autoemporium.models;

import com.autoemporium.autoemporium.models.users.Role;
import com.autoemporium.autoemporium.views.Views;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;


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
    private String producer;

    @JsonView(value = {Views.Level1.class,Views.Level2.class,Views.Level3.class})
    private String model;

    @JsonView(value = {Views.Level1.class,Views.Level2.class})
    private int power;

    @JsonView(value = {Views.Level1.class,Views.Level2.class})
    private int year;

    @JsonView(value = {Views.Level1.class})
    private String color;

    @JsonView(value = {Views.Level1.class})
    private int mileage;

    @JsonView(value = {Views.Level1.class,Views.Level2.class})
    private int numberDoors;

    @JsonView(value = {Views.Level1.class,Views.Level2.class})
    private int numberSeats;

    @ElementCollection(fetch = FetchType.EAGER)
    @JsonView(value = {Views.Level1.class,Views.Level2.class})
    private List<String> photo;


    @OneToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "advertisement_id", referencedColumnName = "id")
    private Advertisement advertisement;

    @JsonView(value = Views.Level1.class)
    private int createdBySellerId;

//    public Car(String producer, String model,  int power) {
//        this.producer = producer;
//        this.model = model;
//        this.power = power;
//    }

    public Car(String producer, String model, int power, int year, String color, int mileage, int numberDoors, int numberSeats, int createdBySellerId) {
        this.producer = producer;
        this.model = model;
        this.power = power;
        this.year = year;
        this.color = color;
        this.mileage = mileage;
        this.numberDoors = numberDoors;
        this.numberSeats = numberSeats;
        this.createdBySellerId = createdBySellerId;
    }

    public Car(String producer, String  model, int power, int year, String color, int mileage, int numberDoors, int numberSeats) {
        this.producer = producer;
        this.model = model;
        this.power = power;
        this.year = year;
        this.color = color;
        this.mileage=mileage;
        this.numberDoors = numberDoors;
        this.numberSeats= numberSeats;
    }
}

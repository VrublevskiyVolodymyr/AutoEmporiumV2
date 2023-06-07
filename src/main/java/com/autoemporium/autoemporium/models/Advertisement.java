package com.autoemporium.autoemporium.models;

import com.autoemporium.autoemporium.views.Views;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity

public class Advertisement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String description;
    private int views;
    private Boolean status;

//    @ManyToOne(fetch = FetchType.EAGER)
//    private Car car;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    private PriceCar priceCar;
    private double price;

    @Enumerated(EnumType.STRING)
    private Currency currency;
    private LocalDateTime createdAt;
    private LocalDateTime editedAt;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinTable(name = "client_adv",
            joinColumns = @JoinColumn(name = "adv_id"),
            inverseJoinColumns = @JoinColumn(name = "client_id")
    )
    private Client createdBy;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinTable(name = "autodealer_adv",
            joinColumns = @JoinColumn(name = "adv_id"),
            inverseJoinColumns = @JoinColumn(name = "autodealer_id")
    )
    private AutoDealer createdByDealer;
    private int editCount;

//    public void addAdvertisement() {
//        // Code to add the advertisement to the database
//    }
//
//    public void editAdvertisement() {
//        // Code to edit the advertisement in the database
//    }
//
//    public Advertisement getAdvertisementData() {
//        // Code to retrieve the advertisement data from the database
//        return this;
//    }
    public Advertisement(String title, String description,double price, int power,Client createdBy,Currency currency) {
        this.title= title;
        this.description = description;
//        this.car = car;
        this.price = price;
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "Advertisement{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", views=" + views +
                ", status=" + status +
                ", price=" + price +
                ", currency=" + currency +
                ", createdAt=" + createdAt +
                ", editedAt=" + editedAt +
                '}';
    }
}

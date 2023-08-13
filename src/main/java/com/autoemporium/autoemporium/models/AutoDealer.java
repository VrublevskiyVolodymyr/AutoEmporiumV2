package com.autoemporium.autoemporium.models;

import com.autoemporium.autoemporium.models.users.*;
import com.autoemporium.autoemporium.views.Views;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {"admins", "managers", "sales", "mechanics", "buyers", "advertisements"})
@Entity
public class AutoDealer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(value = {Views.Level1.class, Views.Level2.class})
    private int id;

    @JsonView(value = {Views.Level1.class, Views.Level2.class, Views.Level3.class})
    private String name;

    @JsonView(value = {Views.Level1.class, Views.Level2.class, Views.Level3.class})
    private String location;

    @JsonView(value = {Views.Level1.class, Views.Level2.class})
    private LocalDateTime createdAt;


    @JsonView(value = Views.Level1.class)
    @OneToMany(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinTable(name = "autodealer_admins",
            joinColumns = @JoinColumn(name = "autodealer_id"),
            inverseJoinColumns = @JoinColumn(name = "admin_id")
    )
    private List<Administrator> admins;

    @JsonView(value = Views.Level1.class)
    @OneToMany(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinTable(name = "autodealer_managers",
            joinColumns = @JoinColumn(name = "autodealer_id"),
            inverseJoinColumns = @JoinColumn(name = "manager_id")
    )
    private List<Manager> managers = new ArrayList<>();

    @JsonView(value = Views.Level1.class)
    @OneToMany(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinTable(name = "autodealer_sales",
            joinColumns = @JoinColumn(name = "autodealer_id"),
            inverseJoinColumns = @JoinColumn(name = "seller_id")
    )
    private List<Seller> sales = new ArrayList<>();


    @JsonView(value = Views.Level1.class)
    @OneToMany(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinTable(name = "autodealer_mechanics",
            joinColumns = @JoinColumn(name = "autodealer_id"),
            inverseJoinColumns = @JoinColumn(name = "mechanic_id")
    )
    private List<Mechanic> mechanics = new ArrayList<>();

    @JsonView(value = Views.Level1.class)
    @OneToMany(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinTable(name = "autodealer_buyers",
            joinColumns = @JoinColumn(name = "autodealer_id"),
            inverseJoinColumns = @JoinColumn(name = "buyer_id")
    )
    private List<Buyer> buyers = new ArrayList<>();

    @JsonIgnore
    @JsonView(value = Views.Level1.class)
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "autodealer_adv",
            joinColumns = @JoinColumn(name = "autodealer_id"),
            inverseJoinColumns = @JoinColumn(name = "adv_id")
    )
    private List<Advertisement> advertisements = new ArrayList<>();
}

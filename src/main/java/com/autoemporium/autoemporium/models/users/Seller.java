package com.autoemporium.autoemporium.models.users;

import com.autoemporium.autoemporium.models.Advertisement;
import com.autoemporium.autoemporium.views.Views;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


import java.time.LocalDateTime;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Seller {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(value = Views.Level1.class)
    private int id;

    @NotBlank(message = "firstName cannot be empty")
    private String firstName;

    @NotBlank(message = "lastName cannot be empty")
    private String lastName;

    @Column(unique = true)
    @JsonView(value = Views.Level3.class)
    @NotBlank(message = "phoneNumber cannot be empty")
    private String phone;

    @Enumerated(EnumType.STRING)
    @JsonView(value = Views.Level1.class)
    private AccountType accountType = AccountType.BASIC;

    @Enumerated(EnumType.STRING)
    @JsonView(value = Views.Level1.class)
    private Status status;

    @JsonView(value = Views.Level1.class)
    private int countOfAds;

    @JsonView(value = Views.Level1.class)
    private int autoDealer_id;

    @JsonView(value = Views.Level1.class)
    private LocalDateTime createdAt;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonView(value = Views.Level1.class)
    private User user;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "seller_adv",
            joinColumns = @JoinColumn(name = "seller_id"),
            inverseJoinColumns = @JoinColumn(name = "adv_id")
    )
    private List<Advertisement> advertisements;


    public Seller (String firstname,String lastname, String phone, String username, String password, List<Role> roles) {
        this.firstName=firstname;
        this.lastName=lastname;
        this.phone=phone;
        this.user = new User(username, password, List.of(Role.SELLER));
    }
}

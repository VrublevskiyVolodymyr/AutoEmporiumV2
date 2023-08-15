package com.autoemporium.autoemporium.models.users;

import com.autoemporium.autoemporium.views.Views;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
public class
Owner{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private static Owner instance;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonView(value = Views.Level1.class)
    private User user;

    private Owner(String username, String password,String refreshToken, List<Role> roles,Boolean status) {
        this.user = new User(username,password, refreshToken, List.of(Role.ADMIN),status);
    }

    public static Owner getInstance(String username, String password,String refreshToken) {
        if (instance == null) {
            instance = new Owner(username,password,refreshToken,List.of(Role.ADMIN),true);
        }
            return instance;
    }
}


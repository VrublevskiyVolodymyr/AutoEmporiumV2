package com.autoemporium.autoemporium.models.users;

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
@ToString
public class Buyer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(value = Views.Level1.class)
    private int id;

    @Enumerated(EnumType.STRING)
    @JsonView(value = Views.Level1.class)
    private Status status;

    @JsonView(value = Views.Level1.class)
    private LocalDateTime createdAt;

    @JsonView(value = Views.Level1.class)
    private int autoDealer_id;

@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
@JoinColumn(name = "user_id", referencedColumnName = "id")
@JsonView(value = Views.Level1.class)
    private User user;

    public Buyer( String username, String password, List<Role> roles) {
        this.user = new User(username,password,List.of(Role.BUYER));
    }

}

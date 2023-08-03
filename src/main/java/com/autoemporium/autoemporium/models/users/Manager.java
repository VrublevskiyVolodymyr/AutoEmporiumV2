package com.autoemporium.autoemporium.models.users;

import com.autoemporium.autoemporium.views.Views;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@ToString
public class Manager {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(value = Views.Level1.class)
    private int id;

    @JsonView(value = Views.Level1.class)
    private String firstName;

    @JsonView(value = Views.Level1.class)
    private String lastName;

    @JsonView(value = Views.Level1.class)
    @Column(unique = true)
    private String phone;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonView(value = Views.Level1.class)
    private User user;

    public Manager(String firstname,String lastname,String phone, String username, String password, List<Role> roles) {
        this.firstName=firstname;
        this.lastName=lastname;
        this.phone=phone;
        this.user = new User(username,password,List.of(Role.MANAGER));
    }
}

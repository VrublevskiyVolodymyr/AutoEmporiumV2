package com.autoemporium.autoemporium.models;

import com.autoemporium.autoemporium.views.Views;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Builder
@ToString
public class Client implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(value = Views.Level1.class)
    private int id;
    private String firstName;
    private String lastName;
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @JsonView(value = Views.Level1.class)
    private List<Role> roles = List.of(Role.BUYER);
    @Column(unique = true)
    @JsonView(value = Views.Level3.class)
    private String email;
    @JsonView(value = {Views.Level1.class, Views.Level1.class})
    private String password;


    @Enumerated(EnumType.STRING)
    @JsonView(value = Views.Level1.class)
    private AccountType accountType = AccountType.BASIC;

    @Enumerated(EnumType.STRING)
    @JsonView(value = Views.Level1.class)
    private Status status;

    @JsonView(value = Views.Level1.class)
    private int countOfAds;


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "client_adv",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "adv_id")
    )
    private List<Advertisement> advertisements;

    public Client(UserDetails loadUserByUsername) {
    }

    @Override
    @JsonView(value = Views.Level1.class)
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        this.roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role.name())));
        return authorities;
    }

    @Override
    @JsonView(value = Views.Level1.class)
    public String getUsername() {
        return this.email;
    }

    @Override
    @JsonView(value = Views.Level1.class)
    public String getPassword() {
        return this.password;
    }

    @Override
    @JsonView(value = Views.Level1.class)
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonView(value = Views.Level1.class)
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonView(value = Views.Level1.class)
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonView(value = Views.Level1.class)
    public boolean isEnabled() {
        return true;
    }

    @Data
    @AllArgsConstructor
    @Builder
    public static class ClientDTO {
        private String userFirstname;
        private String userLastname;
        private String email;
        private String password;
    }
}

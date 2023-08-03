package com.autoemporium.autoemporium.models.users;

import com.autoemporium.autoemporium.models.Advertisement;
import com.autoemporium.autoemporium.views.Views;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


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


//    public Client(UserDetails loadUserByUsername) {
//    }
//
//    @Override
//    @JsonView(value = Views.Level1.class)
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        List<GrantedAuthority> authorities = new ArrayList<>();
//        this.roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role.name())));
//        return authorities;
//    }
//
//    @Override
//    @JsonView(value = Views.Level1.class)
//    public String getUsername() {
//        return this.email;
//    }
//
//    @Override
//    @JsonView(value = Views.Level1.class)
//    public String getPassword() {
//        return this.password;
//    }
//
//    @Override
//    @JsonView(value = Views.Level1.class)
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    @JsonView(value = Views.Level1.class)
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    @JsonView(value = Views.Level1.class)
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    @JsonView(value = Views.Level1.class)
//    public boolean isEnabled() {
//        return true;
//    }

//    @Data
//    @AllArgsConstructor
//    @Builder
//    public static class ClientDTO {
//        private String userFirstname;
//        private String userLastname;
//        private String email;
//        private String password;
//    }
//}

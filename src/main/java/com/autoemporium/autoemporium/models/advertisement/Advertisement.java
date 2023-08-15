package com.autoemporium.autoemporium.models.advertisement;


import com.autoemporium.autoemporium.models.autodealer.AutoDealer;
import com.autoemporium.autoemporium.models.cars.Car;
import com.autoemporium.autoemporium.models.cars.PriceCar;
import com.autoemporium.autoemporium.models.financial.Currency;
import com.autoemporium.autoemporium.models.users.Seller;


import com.autoemporium.autoemporium.views.Views;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.*;


import java.math.BigDecimal;
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
    @JsonView(value = Views.Level1.class)
    private int id;

    @JsonView(value = {Views.Level1.class, Views.Level2.class, Views.Level3.class})
    private String title;

    @JsonView(value = {Views.Level1.class, Views.Level2.class, Views.Level3.class})
    private String description;

    @JsonView(value = Views.Level1.class)
    private int views;

    @JsonView(value = Views.Level1.class)
    private Boolean status;

    @JsonView(value = {Views.Level1.class, Views.Level2.class, Views.Level3.class})
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "car_id", referencedColumnName = "id")
    private Car car;

    @JsonView(value = {Views.Level1.class, Views.Level2.class, Views.Level3.class})
    private String region;

    @JsonView(value = {Views.Level1.class, Views.Level2.class, Views.Level3.class})
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private PriceCar priceCar;

    @JsonView(value = {Views.Level1.class, Views.Level2.class, Views.Level3.class})
    private BigDecimal price;

    @JsonView(value = {Views.Level1.class, Views.Level2.class, Views.Level3.class})
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @JsonView(value = Views.Level1.class)
    private LocalDateTime createdAt;

    @JsonView(value = Views.Level1.class)
    private LocalDateTime editedAt;

    @JsonIgnore
    @JsonView(value = Views.Level1.class)
    @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinTable(name = "seller_adv",
            joinColumns = @JoinColumn(name = "adv_id"),
            inverseJoinColumns = @JoinColumn(name = "seller_id")
    )
    private Seller createdBySeller;

    @JsonView(value = Views.Level1.class)
    @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinTable(name = "autodealer_adv",
            joinColumns = @JoinColumn(name = "adv_id"),
            inverseJoinColumns = @JoinColumn(name = "autodealer_id")
    )
    private AutoDealer createdByDealer;

    @JsonView(value = Views.Level1.class)
    private int editCount;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "adv_view",
            joinColumns = @JoinColumn(name = "adv_id"),
            inverseJoinColumns = @JoinColumn(name = "view_id")
    )
    private List<AdvertisementView> advertisementViews;


    public Advertisement(String title, String description, BigDecimal price, int power, Seller createdBy, Currency currency, String region) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.currency = currency;
        this.region = region;
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

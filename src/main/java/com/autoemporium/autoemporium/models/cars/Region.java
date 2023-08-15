package com.autoemporium.autoemporium.models.cars;

import com.autoemporium.autoemporium.views.Views;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Region {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(value = {Views.Level1.class,Views.Level2.class,Views.Level3.class})
    private int id;

    @JsonView(value = {Views.Level1.class,Views.Level2.class,Views.Level3.class})
    private String region;

    public Region(String region) {
        this.region=region;
    }
}

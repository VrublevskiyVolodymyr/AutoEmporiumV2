package com.autoemporium.autoemporium.models.cars;

import com.autoemporium.autoemporium.views.Views;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Model {
    @Id
    @JsonView(value = {Views.Level1.class,Views.Level2.class,Views.Level3.class})
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JsonView(value = {Views.Level1.class,Views.Level2.class,Views.Level3.class})
    private String model;

    @JsonView(value = Views.Level1.class)
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "producer_model",
            joinColumns = @JoinColumn(name = "model_id"),
            inverseJoinColumns = @JoinColumn(name = "producer_id")
    )
    private Producer producer;


    public Model(String model) {
        this.model = model;
    }

    @Override
    public String toString() {
        return "Model{" +
                "id=" + id +
                ", model='" + model + '\'' +
                '}';
    }
}

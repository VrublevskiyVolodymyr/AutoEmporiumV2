package com.autoemporium.autoemporium.models;

import com.autoemporium.autoemporium.views.Views;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity

public class Producer {
    @Id
    @JsonView(value = {Views.Level1.class,Views.Level2.class,Views.Level3.class})
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JsonView(value = {Views.Level1.class,Views.Level2.class,Views.Level3.class})
    private String producer;

    @JsonView(value = Views.Level1.class)
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "producer_model",
            joinColumns = @JoinColumn(name = "producer_id"),
            inverseJoinColumns = @JoinColumn(name = "model_id")
    )
    private List<Model> models;

    public Producer(String producer) {
        this.producer = producer;
    }

    @Override
    public String toString() {
        return "Producer{" +
                "id=" + id +
                ", producer='" + producer + '\'' +
                '}';
    }
}


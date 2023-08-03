package com.autoemporium.autoemporium.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "view")
public class AdvertisementView {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinTable(name = "adv_view",
            joinColumns = @JoinColumn(name = "view_id"),
            inverseJoinColumns = @JoinColumn(name = "adv_id")
    )
    private Advertisement advertisement;

    @Column(name = "view_date")
    private LocalDate viewDate;

    @Column(name = "viewer_user_id")
    private int viewerUserId;

    @Column(name = "viewer_owner")
    private Boolean isOwner;
}

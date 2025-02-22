package com.groupe3.pharmaconnect.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "pharmacies")
public class Pharmacy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    private String phone;

    @Column(name = "opening_hours")
    private String openingHours;

    @Column(name = "subscription_active")
    private boolean subscriptionActive;

    @Column(name = "subscription_expiry")
    private LocalDateTime subscriptionExpiry;

    @ManyToOne
    @JoinColumn(name = "pharmacist_id")
    private AppUser pharmacist;

    @OneToMany(mappedBy = "pharmacy", cascade = CascadeType.ALL)
    private Set<Medicament> medicines = new HashSet<>();

    // For geolocation
    private Double latitude;
    private Double longitude;
}
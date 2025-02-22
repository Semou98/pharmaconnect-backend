package com.groupe3.pharmaconnect.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "medicaments")
public class Medicament {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String dosage;

    @Column(name = "side_effects")
    private String sideEffects;

    @Column(name = "prescription_required")
    private boolean prescriptionRequired;

    @Column(name = "quantity_available")
    private Integer quantityAvailable;

    private Double price;

    @ManyToOne
    @JoinColumn(name = "pharmacy_id")
    private Pharmacy pharmacy;
}
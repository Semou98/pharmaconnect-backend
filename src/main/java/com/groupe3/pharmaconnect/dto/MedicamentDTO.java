package com.groupe3.pharmaconnect.dto;

import lombok.Data;

@Data
public class MedicamentDTO {
    private Long id;
    private String name;
    private String dosage;
    private String sideEffects;
    private boolean isPrescriptionRequired;
    private Integer quantityAvailable;
    private Double price;
    private Long pharmacyId;
}
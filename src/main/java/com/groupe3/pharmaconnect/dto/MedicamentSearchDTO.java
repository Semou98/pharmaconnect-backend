package com.groupe3.pharmaconnect.dto;

import lombok.Data;

@Data
public class MedicamentSearchDTO {
    private String name;
    private Double maxDistance;
    private Double latitude;
    private Double longitude;
    private Boolean availableOnly;
}
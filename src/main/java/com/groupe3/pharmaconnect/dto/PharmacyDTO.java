package com.groupe3.pharmaconnect.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PharmacyDTO {
    private Long id;
    private String name;
    private String address;
    private String phone;
    private String openingHours;
    private boolean subscriptionActive;
    private LocalDateTime subscriptionExpiry;
    private Double latitude;
    private Double longitude;
    private Long pharmacistId;
}
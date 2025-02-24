package com.groupe3.pharmaconnect.dto;

import lombok.Data;

@Data
public class PharmacyStatsDTO {
    private int totalMedicaments;
    private int lowStockCount;
    private double averagePrice;
    private int outOfStockCount;
}
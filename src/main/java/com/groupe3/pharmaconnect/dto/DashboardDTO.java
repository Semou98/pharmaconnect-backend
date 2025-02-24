package com.groupe3.pharmaconnect.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class DashboardDTO {
    private List<MedicamentDTO> lowStockMedicaments;
    private int totalMedicaments;
    private boolean subscriptionStatus;
    private LocalDateTime subscriptionExpiryDate;
    private int totalOrders;
    private double monthlyRevenue;
}

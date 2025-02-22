package com.groupe3.pharmaconnect.dto;

import com.groupe3.pharmaconnect.enums.SubscriptionStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SubscriptionDTO {
    private Long id;
    private Long pharmacyId;
    private Double amount;
    private LocalDateTime startDate;
    private LocalDateTime expiryDate;
    private SubscriptionStatus status;
}
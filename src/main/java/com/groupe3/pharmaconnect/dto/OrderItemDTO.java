package com.groupe3.pharmaconnect.dto;

import lombok.Data;

@Data
public class OrderItemDTO {
    private Long id;
    private Long medicineId;
    private Integer quantity;
}
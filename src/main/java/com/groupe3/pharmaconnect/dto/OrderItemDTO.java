package com.groupe3.pharmaconnect.dto;

import lombok.Data;

@Data
public class OrderItemDTO {
    private Long id;
    private Long medicamentId;
    private Integer quantity;
    private Double unitPrice;
    private Double totalPrice;
}
package com.groupe3.pharmaconnect.dto;

import com.groupe3.pharmaconnect.enums.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class OrderDTO {
    private Long id;
    private Long userId;
    private Long pharmacyId;
    private Set<OrderItemDTO> orderItems;
    private OrderStatus status;
    private LocalDateTime orderDate;
    private double totalPrice;
}
package com.groupe3.pharmaconnect.services.order;

import com.groupe3.pharmaconnect.dto.OrderDTO;
import com.groupe3.pharmaconnect.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderDTO createOrder(OrderDTO orderDTO);
    OrderDTO updateOrderStatus(Long id, OrderStatus status);
    OrderDTO getOrderById(Long id);
    Page<OrderDTO> getOrdersByUser(Long userId, Pageable pageable);
    Page<OrderDTO> getOrdersByPharmacy(Long pharmacyId, Pageable pageable);
}
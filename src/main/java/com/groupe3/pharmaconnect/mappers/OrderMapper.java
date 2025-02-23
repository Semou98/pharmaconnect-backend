package com.groupe3.pharmaconnect.mappers;

import com.groupe3.pharmaconnect.dto.OrderDTO;
import com.groupe3.pharmaconnect.dto.OrderItemDTO;
import com.groupe3.pharmaconnect.entities.AppUser;
import com.groupe3.pharmaconnect.entities.Order;
import com.groupe3.pharmaconnect.entities.OrderItem;
import com.groupe3.pharmaconnect.entities.Pharmacy;
import com.groupe3.pharmaconnect.exceptions.MediSearchException;
import com.groupe3.pharmaconnect.repositories.AppUserRepository;
import com.groupe3.pharmaconnect.repositories.PharmacyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderMapper {
    private final AppUserRepository userRepository;
    private final PharmacyRepository pharmacyRepository;
    private final OrderItemMapper orderItemMapper;

    public OrderDTO toDTO(Order order) {
        if (order == null) return null;

        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setUserId(order.getUser().getId());
        dto.setPharmacyId(order.getPharmacy().getId());
        dto.setStatus(order.getStatus());
        dto.setOrderDate(order.getOrderDate());

        // Mapper les items de la commande
        dto.setOrderItems(order.getOrderItems().stream()
                .map(orderItemMapper::toDTO)
                .collect(Collectors.toSet()));

        // Calculer le prix total
        dto.setTotalPrice(order.getOrderItems().stream()
                .mapToDouble(item -> item.getQuantity() * item.getUnitPrice())
                .sum());

        return dto;
    }

    public Order toEntity(OrderDTO dto) {
        if (dto == null) return null;

        Order order = new Order();
        order.setId(dto.getId());
        order.setStatus(dto.getStatus());
        order.setOrderDate(dto.getOrderDate());

        // Récupérer l'utilisateur
        AppUser user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new MediSearchException("Utilisateur non trouvé", HttpStatus.NOT_FOUND));
        order.setUser(user);

        // Récupérer la pharmacie
        Pharmacy pharmacy = pharmacyRepository.findById(dto.getPharmacyId())
                .orElseThrow(() -> new MediSearchException("Pharmacie non trouvée", HttpStatus.NOT_FOUND));
        order.setPharmacy(pharmacy);

        // Mapper les items de la commande
        if (dto.getOrderItems() != null) {
            order.setOrderItems(dto.getOrderItems().stream()
                    .map(orderItemMapper::toEntity)
                    .collect(Collectors.toSet()));
        }

        return order;
    }
}

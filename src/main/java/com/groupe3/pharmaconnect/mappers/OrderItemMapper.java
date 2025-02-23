package com.groupe3.pharmaconnect.mappers;

import com.groupe3.pharmaconnect.dto.OrderItemDTO;
import com.groupe3.pharmaconnect.entities.Medicament;
import com.groupe3.pharmaconnect.entities.OrderItem;
import com.groupe3.pharmaconnect.exceptions.MediSearchException;
import com.groupe3.pharmaconnect.repositories.MedicamentRepository;
import com.groupe3.pharmaconnect.services.medicament.MedicamentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderItemMapper {
    private final MedicamentRepository medicamentRepository;
    private final MedicamentMapper medicamentMapper;

    public OrderItemDTO toDTO(OrderItem orderItem) {
        if (orderItem == null) return null;

        OrderItemDTO dto = new OrderItemDTO();
        dto.setId(orderItem.getId());
        dto.setMedicamentId(orderItem.getMedicament().getId());
        dto.setQuantity(orderItem.getQuantity());
        dto.setUnitPrice(orderItem.getUnitPrice());
        // Calcul du prix total
        dto.setTotalPrice(orderItem.getQuantity() * orderItem.getUnitPrice());

        return dto;
    }

    public OrderItem toEntity(OrderItemDTO dto) {
        if (dto == null) return null;

        // Récupération du médicament via le repository
        Medicament medicament = medicamentRepository.findById(dto.getMedicamentId())
                .orElseThrow(() -> new MediSearchException("Médicament non trouvé", HttpStatus.NOT_FOUND));

        OrderItem orderItem = new OrderItem();
        orderItem.setId(dto.getId());
        orderItem.setMedicament(medicament);
        orderItem.setQuantity(dto.getQuantity());
        orderItem.setUnitPrice(dto.getUnitPrice());

        return orderItem;
    }
}
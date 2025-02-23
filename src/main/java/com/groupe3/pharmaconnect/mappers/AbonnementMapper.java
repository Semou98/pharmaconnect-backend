package com.groupe3.pharmaconnect.mappers;

import com.groupe3.pharmaconnect.dto.SubscriptionDTO;
import com.groupe3.pharmaconnect.entities.Subscription;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AbonnementMapper {
    private final PharmacyMapper pharmacyMapper;

    public SubscriptionDTO toDTO(Subscription subscription) {
        if (subscription == null) return null;

        SubscriptionDTO dto = new SubscriptionDTO();
        dto.setId(subscription.getId());
        dto.setPharmacyId(subscription.getPharmacy().getId());
        dto.setAmount(subscription.getAmount());
        dto.setStartDate(subscription.getStartDate());
        dto.setExpiryDate(subscription.getExpiryDate());
        dto.setStatus(subscription.getStatus());
        return dto;
    }

    public Subscription toEntity(SubscriptionDTO dto) {
        if (dto == null) return null;

        Subscription subscription = new Subscription();
        subscription.setId(dto.getId());
        subscription.setAmount(dto.getAmount());
        subscription.setStartDate(dto.getStartDate());
        subscription.setExpiryDate(dto.getExpiryDate());
        subscription.setStatus(dto.getStatus());
        return subscription;
    }
}
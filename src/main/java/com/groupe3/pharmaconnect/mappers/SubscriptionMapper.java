package com.groupe3.pharmaconnect.mappers;

import com.groupe3.pharmaconnect.dto.SubscriptionDTO;
import com.groupe3.pharmaconnect.entities.Pharmacy;
import com.groupe3.pharmaconnect.entities.Subscription;
import com.groupe3.pharmaconnect.exceptions.MediSearchException;
import com.groupe3.pharmaconnect.repositories.PharmacyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubscriptionMapper {
    private final PharmacyRepository pharmacyRepository;

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

        // Récupérer la pharmacie
        if (dto.getPharmacyId() != null) {
            Pharmacy pharmacy = pharmacyRepository.findById(dto.getPharmacyId())
                    .orElseThrow(() -> new MediSearchException("Pharmacie non trouvée", HttpStatus.NOT_FOUND));
            subscription.setPharmacy(pharmacy);
        }

        return subscription;
    }
}
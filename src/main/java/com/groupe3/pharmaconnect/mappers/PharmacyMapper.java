package com.groupe3.pharmaconnect.mappers;

import com.groupe3.pharmaconnect.dto.PharmacyDTO;
import com.groupe3.pharmaconnect.entities.Pharmacy;
import org.springframework.stereotype.Component;

@Component
public class PharmacyMapper {
    public PharmacyDTO toDTO(Pharmacy pharmacy) {
        if (pharmacy == null) return null;

        PharmacyDTO dto = new PharmacyDTO();
        dto.setId(pharmacy.getId());
        dto.setName(pharmacy.getName());
        dto.setAddress(pharmacy.getAddress());
        dto.setPhone(pharmacy.getPhone());
        dto.setOpeningHours(pharmacy.getOpeningHours());
        dto.setSubscriptionActive(pharmacy.isSubscriptionActive());
        dto.setSubscriptionExpiry(pharmacy.getSubscriptionExpiry());
        dto.setLatitude(pharmacy.getLatitude());
        dto.setLongitude(pharmacy.getLongitude());
        dto.setPharmacistId(pharmacy.getPharmacist().getId());
        return dto;
    }

    public Pharmacy toEntity(PharmacyDTO dto) {
        if (dto == null) return null;

        Pharmacy pharmacy = new Pharmacy();
        pharmacy.setId(dto.getId());
        pharmacy.setName(dto.getName());
        pharmacy.setAddress(dto.getAddress());
        pharmacy.setPhone(dto.getPhone());
        pharmacy.setOpeningHours(dto.getOpeningHours());
        pharmacy.setSubscriptionActive(dto.isSubscriptionActive());
        pharmacy.setSubscriptionExpiry(dto.getSubscriptionExpiry());
        pharmacy.setLatitude(dto.getLatitude());
        pharmacy.setLongitude(dto.getLongitude());
        return pharmacy;
    }
}
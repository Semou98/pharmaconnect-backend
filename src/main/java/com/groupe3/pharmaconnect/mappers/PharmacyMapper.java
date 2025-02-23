package com.groupe3.pharmaconnect.mappers;

import com.groupe3.pharmaconnect.dto.PharmacyDTO;
import com.groupe3.pharmaconnect.entities.AppUser;
import com.groupe3.pharmaconnect.entities.Medicament;
import com.groupe3.pharmaconnect.entities.Pharmacy;
import com.groupe3.pharmaconnect.exceptions.MediSearchException;
import com.groupe3.pharmaconnect.repositories.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PharmacyMapper {
    private final AppUserRepository userRepository;

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
        dto.setPharmacistId(pharmacy.getPharmacist().getId());
        dto.setLatitude(pharmacy.getLatitude());
        dto.setLongitude(pharmacy.getLongitude());

        // Mapper uniquement les IDs des médicaments
        dto.setMedicineIds(pharmacy.getMedicines().stream()
                .map(Medicament::getId)
                .collect(Collectors.toSet()));

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

        // Récupérer le pharmacien
        if (dto.getPharmacistId() != null) {
            AppUser pharmacist = userRepository.findById(dto.getPharmacistId())
                    .orElseThrow(() -> new MediSearchException("Pharmacien non trouvé", HttpStatus.NOT_FOUND));
            pharmacy.setPharmacist(pharmacist);
        }

        return pharmacy;
    }
}
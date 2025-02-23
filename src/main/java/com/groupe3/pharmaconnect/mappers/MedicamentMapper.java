package com.groupe3.pharmaconnect.mappers;

import com.groupe3.pharmaconnect.dto.MedicamentDTO;
import com.groupe3.pharmaconnect.entities.Medicament;
import com.groupe3.pharmaconnect.entities.Pharmacy;
import com.groupe3.pharmaconnect.exceptions.MediSearchException;
import com.groupe3.pharmaconnect.repositories.PharmacyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MedicamentMapper {
    private final PharmacyRepository pharmacyRepository;

    public MedicamentDTO toDTO(Medicament medicament) {
        if (medicament == null) return null;

        MedicamentDTO dto = new MedicamentDTO();
        dto.setId(medicament.getId());
        dto.setName(medicament.getName());
        dto.setDosage(medicament.getDosage());
        dto.setSideEffects(medicament.getSideEffects());
        dto.setPrescriptionRequired(medicament.isPrescriptionRequired());
        dto.setQuantityAvailable(medicament.getQuantityAvailable());
        dto.setPrice(medicament.getPrice());
        dto.setPharmacyId(medicament.getPharmacy() != null ?
                medicament.getPharmacy().getId() : null);

        return dto;
    }

    public Medicament toEntity(MedicamentDTO dto) {
        if (dto == null) return null;

        Medicament medicament = new Medicament();
        medicament.setId(dto.getId());
        medicament.setName(dto.getName());
        medicament.setDosage(dto.getDosage());
        medicament.setSideEffects(dto.getSideEffects());
        medicament.setPrescriptionRequired(dto.isPrescriptionRequired());
        medicament.setQuantityAvailable(dto.getQuantityAvailable());
        medicament.setPrice(dto.getPrice());

        if (dto.getPharmacyId() != null) {
            Pharmacy pharmacy = pharmacyRepository.findById(dto.getPharmacyId())
                    .orElseThrow(() -> new MediSearchException("Pharmacie non trouvée", HttpStatus.NOT_FOUND));
            medicament.setPharmacy(pharmacy);
        }

        return medicament;
    }
}
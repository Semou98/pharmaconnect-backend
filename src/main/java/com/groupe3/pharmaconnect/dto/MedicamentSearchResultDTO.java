package com.groupe3.pharmaconnect.dto;

import com.groupe3.pharmaconnect.entities.Medicament;
import lombok.Data;

@Data
public class MedicamentSearchResultDTO {
    private Medicament medicine;
    private PharmacyDTO pharmacy;
    private Double distance;
}
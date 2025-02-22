package com.groupe3.pharmaconnect.services.medicament;

import com.groupe3.pharmaconnect.dto.MedicamentDTO;
import com.groupe3.pharmaconnect.dto.MedicamentSearchDTO;
import com.groupe3.pharmaconnect.dto.MedicamentSearchResultDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MedicamentService {
    MedicamentDTO createMedicine(MedicamentDTO medicineDTO);
    MedicamentDTO updateMedicine(MedicamentDTO doctorDTO); ///(Long id, MedicineDTO medicineDTO);
    void deleteMedicine(Long id);
    MedicamentDTO getMedicineById(Long id);
    Page<MedicamentDTO> getMedicinesByPharmacy(Long pharmacyId, Pageable pageable);
    Page<MedicamentSearchResultDTO> searchMedicines(MedicamentSearchDTO searchDTO, Pageable pageable);
}
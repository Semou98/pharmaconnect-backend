package com.groupe3.pharmaconnect.services.medicament;

import com.groupe3.pharmaconnect.dto.MedicamentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MedicamentService {
    MedicamentDTO createMedicament(MedicamentDTO medicamentDTO);
    MedicamentDTO updateMedicament(Long id, MedicamentDTO medicamentDTO);
    void deleteMedicament(Long id);
    MedicamentDTO getMedicamentById(Long id);
    Page<MedicamentDTO> getAllMedicaments(Pageable pageable);
    Page<MedicamentDTO> searchMedicaments(String query, Pageable pageable);
    List<MedicamentDTO> getMedicamentsByPharmacy(Long pharmacyId);
    void updateStock(Long medicamentId, Integer quantity);
    List<MedicamentDTO> getAvailableMedicaments(String name, Double maxDistance, Double latitude, Double longitude);
}
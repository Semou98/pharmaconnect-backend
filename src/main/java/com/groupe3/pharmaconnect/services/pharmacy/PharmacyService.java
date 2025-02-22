package com.groupe3.pharmaconnect.services.pharmacy;


import com.groupe3.pharmaconnect.dto.PharmacyDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PharmacyService {
    PharmacyDTO createPharmacy(PharmacyDTO pharmacyDTO);
    PharmacyDTO updatePharmacy(Long id, PharmacyDTO pharmacyDTO);
    void deletePharmacy(Long id);
    PharmacyDTO getPharmacyById(Long id);
    Page<PharmacyDTO> getPharmaciesByPharmacist(Long pharmacistId, Pageable pageable);
    Page<PharmacyDTO> findNearbyPharmacies(Double maxDistance, Double latitude, Double longitude, Pageable pageable);
}
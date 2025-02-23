package com.groupe3.pharmaconnect.services.medicament;

import com.groupe3.pharmaconnect.dto.MedicamentDTO;
import com.groupe3.pharmaconnect.entities.Medicament;
import com.groupe3.pharmaconnect.entities.Pharmacy;
import com.groupe3.pharmaconnect.mappers.MedicamentMapper;
import com.groupe3.pharmaconnect.repositories.MedicamentRepository;
import com.groupe3.pharmaconnect.repositories.PharmacyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class MedicamentServiceImpl implements MedicamentService {
    private final MedicamentRepository medicamentRepository;
    private final PharmacyRepository pharmacyRepository;
    private final MedicamentMapper medicamentMapper;

    @Override
    public MedicamentDTO createMedicament(MedicamentDTO medicamentDTO) {
        validatePharmacySubscription(medicamentDTO.getPharmacyId());
        Medicament medicament = medicamentMapper.toEntity(medicamentDTO);
        return medicamentMapper.toDTO(medicamentRepository.save(medicament));
    }

    @Override
    public void updateStock(Long medicamentId, Integer quantity) {
        Medicament medicament = medicamentRepository.findById(medicamentId)
                .orElseThrow(() -> new ResourceNotFoundException("Medicament not found"));

        validatePharmacySubscription(medicament.getPharmacy().getId());
        medicament.setQuantityAvailable(quantity);
        medicamentRepository.save(medicament);
    }

    @Override
    public Page<MedicamentDTO> searchMedicaments(String query, Pageable pageable) {
        // Recherche par nom avec prise en compte des pharmacies actives
        return medicamentRepository.findByNameContainingIgnoreCaseAndPharmacy_SubscriptionActiveTrue(query, pageable)
                .map(medicamentMapper::toDTO);
    }

    private void validatePharmacySubscription(Long pharmacyId) {
        Pharmacy pharmacy = pharmacyRepository.findById(pharmacyId)
                .orElseThrow(() -> new ResourceNotFoundException("Pharmacy not found"));

        if (!pharmacy.isSubscriptionActive()) {
            throw new BusinessException("Pharmacy subscription is not active");
        }
    }
}
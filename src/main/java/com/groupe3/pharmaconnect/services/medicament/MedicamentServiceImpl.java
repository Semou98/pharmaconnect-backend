package com.groupe3.pharmaconnect.services.medicament;

import com.groupe3.pharmaconnect.dto.MedicamentDTO;
import com.groupe3.pharmaconnect.entities.Medicament;
import com.groupe3.pharmaconnect.entities.Pharmacy;
import com.groupe3.pharmaconnect.exceptions.BusinessException;
import com.groupe3.pharmaconnect.mappers.MedicamentMapper;
import com.groupe3.pharmaconnect.repositories.MedicamentRepository;
import com.groupe3.pharmaconnect.repositories.PharmacyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Range;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
    public MedicamentDTO updateMedicament(Long id, MedicamentDTO medicamentDTO) {
        Medicament existingMedicament = medicamentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medicament not found"));

        validatePharmacySubscription(existingMedicament.getPharmacy().getId());

        Medicament updatedMedicament = medicamentMapper.toEntity(medicamentDTO);
        updatedMedicament.setId(id);
        updatedMedicament.setPharmacy(existingMedicament.getPharmacy());

        return medicamentMapper.toDTO(medicamentRepository.save(updatedMedicament));
    }

    @Override
    public void deleteMedicament(Long id) {
        Medicament medicament = medicamentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medicament not found"));

        validatePharmacySubscription(medicament.getPharmacy().getId());
        medicamentRepository.delete(medicament);
    }

    @Override
    public MedicamentDTO getMedicamentById(Long id) {
        return medicamentRepository.findById(id)
                .map(medicamentMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Medicament not found"));
    }

    @Override
    public List<MedicamentDTO> getMedicamentsByPharmacy(Long pharmacyId) {
        Pharmacy pharmacy = pharmacyRepository.findById(pharmacyId)
                .orElseThrow(() -> new ResourceNotFoundException("Pharmacy not found"));

        List<Medicament> medicaments = medicamentRepository.findByPharmacy(pharmacy);
        return medicaments.stream()
                .map(medicamentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<MedicamentDTO> getAvailableMedicaments(String name, Double maxDistance, Double latitude, Double longitude) {
        // Si les coordonnées ne sont pas fournies, recherche uniquement par nom
        List<Medicament> medicaments;
        if (latitude == null || longitude == null || maxDistance == null) {
            medicaments = medicamentRepository
                    .findByNameContainingIgnoreCaseAndQuantityAvailableGreaterThanAndPharmacy_SubscriptionActiveTrue(name, 0);
        } else {
            // Recherche par nom et distance
            medicaments = medicamentRepository
                    .findAvailableMedicamentsByNameAndDistance(name, maxDistance, latitude, longitude);
        }

        return medicaments.stream()
                .map(medicamentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void updateStock(Long medicamentId, Integer quantity) {
        Medicament medicament = medicamentRepository.findById(medicamentId)
                .orElseThrow(() -> new ResourceNotFoundException("Medicament not found"));

        validatePharmacySubscription(medicament.getPharmacy().getId());
        medicament.setQuantityAvailable(quantity);
        medicamentRepository.save(medicament);
    }

    private void validatePharmacySubscription(Long pharmacyId) {
        Pharmacy pharmacy = pharmacyRepository.findById(pharmacyId)
                .orElseThrow(() -> new ResourceNotFoundException("Pharmacy not found"));

        if (!pharmacy.isSubscriptionActive()) {
            throw new BusinessException("Pharmacy subscription is not active");
        }
    }

    @Override
    public Page<MedicamentDTO> getAllMedicaments(Pageable pageable) {
        return medicamentRepository.findAll(pageable)
                .map(medicamentMapper::toDTO);
    }

    @Override
    public Page<MedicamentDTO> searchMedicaments(String query, Pageable pageable) {
        return medicamentRepository.findByNameContainingIgnoreCaseAndPharmacy_SubscriptionActiveTrue(query, pageable)
                .map(medicamentMapper::toDTO);
    }
}
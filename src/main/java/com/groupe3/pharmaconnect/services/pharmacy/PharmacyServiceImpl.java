package com.groupe3.pharmaconnect.services.pharmacy;

import com.groupe3.pharmaconnect.dto.DashboardDTO;
import com.groupe3.pharmaconnect.dto.MedicamentDTO;
import com.groupe3.pharmaconnect.dto.PharmacyDTO;
import com.groupe3.pharmaconnect.entities.Pharmacy;
import com.groupe3.pharmaconnect.exceptions.BusinessException;
import com.groupe3.pharmaconnect.mappers.MedicamentMapper;
import com.groupe3.pharmaconnect.mappers.PharmacyMapper;
import com.groupe3.pharmaconnect.repositories.PharmacyRepository;
import com.groupe3.pharmaconnect.services.subscription.SubscriptionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PharmacyServiceImpl implements PharmacyService {
    private final PharmacyRepository pharmacyRepository;
    private final PharmacyMapper pharmacyMapper;
    private final SubscriptionService subscriptionService;
    private final MedicamentMapper medicamentMapper;

    @Override
    public PharmacyDTO createPharmacy(PharmacyDTO pharmacyDTO) {
        validatePharmacyData(pharmacyDTO);
        Pharmacy pharmacy = pharmacyMapper.toEntity(pharmacyDTO);
        pharmacy.setSubscriptionActive(false);
        return pharmacyMapper.toDTO(pharmacyRepository.save(pharmacy));
    }

    @Override
    public PharmacyDTO updatePharmacy(Long id, PharmacyDTO pharmacyDTO) {
        Pharmacy existingPharmacy = pharmacyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pharmacy not found with id: " + id));

        updatePharmacyFields(existingPharmacy, pharmacyDTO);
        return pharmacyMapper.toDTO(pharmacyRepository.save(existingPharmacy));
    }

    @Override
    public void deletePharmacy(Long id) {
        Pharmacy pharmacy = pharmacyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pharmacy not found with id: " + id));

        // Vérifier s'il y a des médicaments en stock
        if (!pharmacy.getMedicines().isEmpty()) {
            throw new BusinessException("Cannot delete pharmacy with existing medications");
        }

        pharmacyRepository.delete(pharmacy);
    }

    @Override
    public PharmacyDTO getPharmacyById(Long id) {
        return pharmacyRepository.findById(id)
                .map(pharmacyMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Pharmacy not found with id: " + id));
    }

    @Override
    public Page<PharmacyDTO> getPharmaciesByPharmacist(Long pharmacistId, Pageable pageable) {
        return pharmacyRepository.findByPharmacistId(pharmacistId, pageable)
                .map(pharmacyMapper::toDTO);
    }

    @Override
    public Page<PharmacyDTO> findNearbyPharmacies(Double maxDistance, Double latitude, Double longitude, Pageable pageable) {
        return pharmacyRepository.findNearbyPharmacies(maxDistance, latitude, longitude, pageable)
                .map(pharmacyMapper::toDTO);
    }

    @Override
    public List<PharmacyDTO> getPharmaciesByLocation(Double latitude, Double longitude, Double radiusInKm) {
        if (latitude == null || longitude == null || radiusInKm == null) {
            throw new BusinessException("Latitude, longitude and radius are required");
        }

        double earthRadius = 6371; // Rayon de la Terre en km
        return pharmacyRepository.findAll().stream()
                .filter(Pharmacy::isSubscriptionActive)
                .filter(pharmacy -> calculateDistance(latitude, longitude,
                        pharmacy.getLatitude(), pharmacy.getLongitude(), earthRadius) <= radiusInKm)
                .map(pharmacyMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DashboardDTO getPharmacyDashboard(Long pharmacyId) {
        Pharmacy pharmacy = pharmacyRepository.findById(pharmacyId)
                .orElseThrow(() -> new ResourceNotFoundException("Pharmacy not found"));

        return DashboardDTO.builder()
                .lowStockMedicaments(getLowStockMedicaments(pharmacy))
                .totalMedicaments(pharmacy.getMedicines().size())
                .subscriptionStatus(pharmacy.isSubscriptionActive())
                .subscriptionExpiryDate(pharmacy.getSubscriptionExpiry())
                .build();
    }

    private List<MedicamentDTO> getLowStockMedicaments(Pharmacy pharmacy) {
        return pharmacy.getMedicines().stream()
                .filter(med -> med.getQuantityAvailable() < 10)
                .map(medicamentMapper::toDTO)
                .collect(Collectors.toList());
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2, double earthRadius) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return earthRadius * c;
    }

    private void validatePharmacyData(PharmacyDTO pharmacyDTO) {
        if (pharmacyDTO.getName() == null || pharmacyDTO.getName().trim().isEmpty()) {
            throw new BusinessException("Le nom de la pharmacie est obligatoire");
        }
        if (pharmacyDTO.getAddress() == null || pharmacyDTO.getAddress().trim().isEmpty()) {
            throw new BusinessException("L'adresse de la pharmacie est obligatoire");
        }
        if (pharmacyDTO.getLatitude() == null || pharmacyDTO.getLongitude() == null) {
            throw new BusinessException("Les coordonnées géographiques sont obligatoires");
        }
        if (pharmacyDTO.getPhone() != null && !pharmacyDTO.getPhone().matches("^[+]?[0-9]{9,15}$")) {
            throw new BusinessException("Format de numéro de téléphone invalide");
        }
    }

    private void updatePharmacyFields(Pharmacy existingPharmacy, PharmacyDTO pharmacyDTO) {
        existingPharmacy.setName(pharmacyDTO.getName());
        existingPharmacy.setAddress(pharmacyDTO.getAddress());
        existingPharmacy.setPhone(pharmacyDTO.getPhone());
        existingPharmacy.setOpeningHours(pharmacyDTO.getOpeningHours());
        existingPharmacy.setLatitude(pharmacyDTO.getLatitude());
        existingPharmacy.setLongitude(pharmacyDTO.getLongitude());
    }
}
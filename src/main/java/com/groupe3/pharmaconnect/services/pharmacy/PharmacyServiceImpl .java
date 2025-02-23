package com.groupe3.pharmaconnect.services.pharmacy;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PharmacyServiceImpl implements PharmacyService {
    private final PharmacyRepository pharmacyRepository;
    private final PharmacyMapper pharmacyMapper;
    private final SubscriptionService subscriptionService;
    
    @Override
    public PharmacyDTO createPharmacy(PharmacyDTO pharmacyDTO) {
        validatePharmacyData(pharmacyDTO);
        Pharmacy pharmacy = pharmacyMapper.toEntity(pharmacyDTO);
        pharmacy.setSubscriptionActive(false); // Par défaut inactif jusqu'à souscription
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
    public List<PharmacyDTO> getPharmaciesByLocation(Double latitude, Double longitude, Double radiusInKm) {
        // Utilisation de la formule de Haversine pour calculer la distance
        double earthRadius = 6371; // Rayon de la Terre en km
        
        return pharmacyRepository.findAll().stream()
            .filter(pharmacy -> pharmacy.isSubscriptionActive())
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
            .filter(med -> med.getQuantityAvailable() < 10) // Seuil d'alerte configurable
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
}
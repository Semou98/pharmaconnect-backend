package com.groupe3.pharmaconnect.services.medicament;

import com.groupe3.pharmaconnect.dto.MedicamentDTO;
import com.groupe3.pharmaconnect.dto.MedicamentSearchDTO;
import com.groupe3.pharmaconnect.dto.MedicamentSearchResultDTO;
import com.groupe3.pharmaconnect.dto.SubscriptionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SubscriptionService {
    SubscriptionDTO createSubscription(SubscriptionDTO subscriptionDTO);
    SubscriptionDTO renewSubscription(Long pharmacyId);
    void cancelSubscription(Long pharmacyId);
    SubscriptionDTO getSubscriptionByPharmacy(Long pharmacyId);
    void checkAndUpdateSubscriptionStatus();  // Pour le scheduling
    void simulatePayment(Long pharmacyId, Double amount);
}
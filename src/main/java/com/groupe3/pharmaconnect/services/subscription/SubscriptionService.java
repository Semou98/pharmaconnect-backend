package com.groupe3.pharmaconnect.services.subscription;

import com.groupe3.pharmaconnect.dto.SubscriptionDTO;

public interface SubscriptionService {
    SubscriptionDTO createSubscription(SubscriptionDTO subscriptionDTO);
    SubscriptionDTO renewSubscription(Long pharmacyId);
    void cancelSubscription(Long pharmacyId);
    SubscriptionDTO getSubscriptionByPharmacy(Long pharmacyId);
    void checkAndUpdateSubscriptionStatus();  // Pour le scheduling
    void simulatePayment(Long pharmacyId, Double amount);
}
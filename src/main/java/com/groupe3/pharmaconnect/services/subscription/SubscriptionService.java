package com.groupe3.pharmaconnect.services.subscription;

import com.groupe3.pharmaconnect.dto.SubscriptionDTO;

public interface SubscriptionService {
    SubscriptionDTO createSubscription(SubscriptionDTO subscriptionDTO);
    SubscriptionDTO renewSubscription(Long pharmacyId);
    void checkAndUpdateSubscriptionStatus(Long pharmacyId);
    SubscriptionDTO getActiveSubscription(Long pharmacyId);
}
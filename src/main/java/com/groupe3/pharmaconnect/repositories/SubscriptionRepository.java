package com.groupe3.pharmaconnect.repositories;

import com.groupe3.pharmaconnect.entities.Subscription;
import com.groupe3.pharmaconnect.enums.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Optional<Subscription> findByPharmacyIdAndStatus(Long pharmacyId, SubscriptionStatus status);
}
package com.groupe3.pharmaconnect.repositories;

import com.groupe3.pharmaconnect.entities.Pharmacy;
import com.groupe3.pharmaconnect.entities.Subscription;
import com.groupe3.pharmaconnect.enums.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Optional<Subscription> findByPharmacyIdAndStatus(Long pharmacyId, SubscriptionStatus status);

    Optional<Subscription> findTopByPharmacyOrderByExpiryDateDesc(Pharmacy pharmacy);

    List<Subscription> findByStatusAndExpiryDateBefore(SubscriptionStatus subscriptionStatus, LocalDateTime now);

    List<Subscription> findByStatusAndExpiryDateBetween(SubscriptionStatus subscriptionStatus, LocalDateTime now, LocalDateTime localDateTime);
}
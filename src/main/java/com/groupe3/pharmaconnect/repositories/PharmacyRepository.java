package com.groupe3.pharmaconnect.repositories;

import com.groupe3.pharmaconnect.entities.Pharmacy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PharmacyRepository extends JpaRepository<Pharmacy, Long> {
    Page<Pharmacy> findByPharmacistId(Long pharmacistId, Pageable pageable);

    @Query("""
        SELECT p FROM Pharmacy p
        WHERE p.subscriptionActive = true
        AND (?1 IS NULL OR 
            (6371 * acos(cos(radians(?2)) * cos(radians(p.latitude))
            * cos(radians(p.longitude) - radians(?3))
            + sin(radians(?2)) * sin(radians(p.latitude)))) <= ?1)
    """)
    Page<Pharmacy> findNearbyPharmacies(Double maxDistance, Double latitude, Double longitude, Pageable pageable);
}
package com.groupe3.pharmaconnect.repositories;

import com.groupe3.pharmaconnect.entities.Medicament;
import com.groupe3.pharmaconnect.entities.Pharmacy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Range;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public interface MedicamentRepository extends JpaRepository<Medicament, Long> {
    Page<Medicament> findByPharmacyId(Long pharmacyId, Pageable pageable);

    @Query("""
    SELECT m FROM Medicament m
    JOIN m.pharmacy p
    WHERE p.subscriptionActive = true
    AND LOWER(m.name) LIKE LOWER(CONCAT('%', ?1, '%'))
    AND (?2 IS NULL OR m.quantityAvailable > 0)
    """)
    Page<Medicament> searchMedicament(String name, Boolean availableOnly, Pageable pageable);

    Page<Medicament> findByNameContainingIgnoreCaseAndPharmacy_SubscriptionActiveTrue(String query, Pageable pageable);

    Page<Medicament> findByNameContainingIgnoreCase(String name, Pageable pageable);

    List<Medicament> findByNameContainingIgnoreCaseAndQuantityAvailableGreaterThanAndPharmacy_SubscriptionActiveTrue(
            String name, Integer minQuantity);

    @Query("""
    SELECT m FROM Medicament m
    JOIN m.pharmacy p
    WHERE LOWER(m.name) LIKE LOWER(CONCAT('%', :name, '%'))
    AND m.quantityAvailable > 0
    AND p.subscriptionActive = true
    AND (:maxDistance IS NULL OR 
        function('ST_Distance_Sphere',
            function('POINT', p.longitude, p.latitude),
            function('POINT', :longitude, :latitude)
        ) <= :maxDistance * 1000)
    """)
    List<Medicament> findAvailableMedicamentsByNameAndDistance(
            @Param("name") String name,
            @Param("maxDistance") Double maxDistance,
            @Param("latitude") Double latitude,
            @Param("longitude") Double longitude);

    List<Medicament> findByPharmacy(Pharmacy pharmacy);
}

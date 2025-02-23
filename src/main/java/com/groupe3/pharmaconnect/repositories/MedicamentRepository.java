package com.groupe3.pharmaconnect.repositories;

import com.groupe3.pharmaconnect.entities.Medicament;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Range;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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

    <T> Range<T> findByNameContainingIgnoreCaseAndPharmacy_SubscriptionActiveTrue(String query, Pageable pageable);
}
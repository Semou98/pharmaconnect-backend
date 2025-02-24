package com.groupe3.pharmaconnect.controllers;

import com.groupe3.pharmaconnect.dto.DashboardDTO;
import com.groupe3.pharmaconnect.dto.PharmacyDTO;
import com.groupe3.pharmaconnect.services.pharmacy.PharmacyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pharmacies")
@RequiredArgsConstructor
public class PharmacyController {
    private final PharmacyService pharmacyService;

    @PostMapping
    @PreAuthorize("hasRole('PHARMACIST')")
    public ResponseEntity<PharmacyDTO> createPharmacy(@Valid @RequestBody PharmacyDTO pharmacyDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(pharmacyService.createPharmacy(pharmacyDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('PHARMACIST') and @pharmacySecurityService.isPharmacyOwner(#id, principal)")
    public ResponseEntity<PharmacyDTO> updatePharmacy(
            @PathVariable Long id,
            @Valid @RequestBody PharmacyDTO pharmacyDTO) {
        return ResponseEntity.ok(pharmacyService.updatePharmacy(id, pharmacyDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('PHARMACIST') and @pharmacySecurityService.isPharmacyOwner(#id, principal)")
    public ResponseEntity<Void> deletePharmacy(@PathVariable Long id) {
        pharmacyService.deletePharmacy(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PharmacyDTO> getPharmacy(@PathVariable Long id) {
        return ResponseEntity.ok(pharmacyService.getPharmacyById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<PharmacyDTO>> searchPharmaciesByLocation(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam Double radius) {
        return ResponseEntity.ok(pharmacyService.getPharmaciesByLocation(latitude, longitude, radius));
    }

    @GetMapping("/dashboard/{pharmacyId}")
    @PreAuthorize("hasRole('PHARMACIST') and @pharmacySecurityService.isPharmacyOwner(#pharmacyId, principal)")
    public ResponseEntity<DashboardDTO> getPharmacyDashboard(@PathVariable Long pharmacyId) {
        return ResponseEntity.ok(pharmacyService.getPharmacyDashboard(pharmacyId));
    }

    @GetMapping("/pharmacist/{pharmacistId}")
    @PreAuthorize("hasRole('PHARMACIST') and #pharmacistId == principal.id")
    public ResponseEntity<Page<PharmacyDTO>> getPharmacistPharmacies(
            @PathVariable Long pharmacistId,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(pharmacyService.getPharmaciesByPharmacist(pharmacistId, pageable));
    }

    @GetMapping("/nearby")
    public ResponseEntity<Page<PharmacyDTO>> getNearbyPharmacies(
            @RequestParam Double maxDistance,
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(pharmacyService.findNearbyPharmacies(maxDistance, latitude, longitude, pageable));
    }
}
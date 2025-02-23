package com.groupe3.pharmaconnect.controllers;

import com.groupe3.pharmaconnect.dto.MedicamentDTO;
import com.groupe3.pharmaconnect.services.medicament.MedicamentService;
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

public class MedicamentC@RestController
@RequestMapping("/api/v1/medicaments")
@RequiredArgsConstructor
public class MedicamentController {
    private final MedicamentService medicamentService;

    @PostMapping
    @PreAuthorize("hasRole('PHARMACIST') and @pharmacySecurityService.isPharmacyOwner(#medicamentDTO.pharmacyId, principal)")
    public ResponseEntity<MedicamentDTO> createMedicament(@Valid @RequestBody MedicamentDTO medicamentDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(medicamentService.createMedicament(medicamentDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('PHARMACIST') and @pharmacySecurityService.isMedicamentOwner(#id, principal)")
    public ResponseEntity<MedicamentDTO> updateMedicament(
            @PathVariable Long id,
            @Valid @RequestBody MedicamentDTO medicamentDTO) {
        return ResponseEntity.ok(medicamentService.updateMedicament(id, medicamentDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('PHARMACIST') and @pharmacySecurityService.isMedicamentOwner(#id, principal)")
    public ResponseEntity<Void> deleteMedicament(@PathVariable Long id) {
        medicamentService.deleteMedicament(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicamentDTO> getMedicament(@PathVariable Long id) {
        return ResponseEntity.ok(medicamentService.getMedicamentById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<MedicamentDTO>> searchMedicaments(
            @RequestParam String query,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(medicamentService.searchMedicaments(query, pageable));
    }

    @GetMapping("/pharmacy/{pharmacyId}")
    public ResponseEntity<List<MedicamentDTO>> getMedicamentsByPharmacy(@PathVariable Long pharmacyId) {
        return ResponseEntity.ok(medicamentService.getMedicamentsByPharmacy(pharmacyId));
    }

    @PatchMapping("/{id}/stock")
    @PreAuthorize("hasRole('PHARMACIST') and @pharmacySecurityService.isMedicamentOwner(#id, principal)")
    public ResponseEntity<Void> updateStock(
            @PathVariable Long id,
            @RequestParam Integer quantity) {
        medicamentService.updateStock(id, quantity);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/available")
    public ResponseEntity<List<MedicamentDTO>> getAvailableMedicaments(
            @RequestParam String name,
            @RequestParam(required = false) Double maxDistance,
            @RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double longitude) {
        return ResponseEntity.ok(
                medicamentService.getAvailableMedicaments(name, maxDistance, latitude, longitude));
    }
}

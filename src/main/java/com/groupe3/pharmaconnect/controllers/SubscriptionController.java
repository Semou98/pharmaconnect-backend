package com.groupe3.pharmaconnect.controllers;

import com.groupe3.pharmaconnect.dto.SubscriptionDTO;
import com.groupe3.pharmaconnect.services.subscription.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @PostMapping
    @PreAuthorize("hasRole('PHARMACIST') and @pharmacySecurityService.isPharmacyOwner(#subscriptionDTO.pharmacyId, principal)")
    public ResponseEntity<SubscriptionDTO> createSubscription(
            @Valid @RequestBody SubscriptionDTO subscriptionDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(subscriptionService.createSubscription(subscriptionDTO));
    }

    @PostMapping("/{pharmacyId}/renew")
    @PreAuthorize("hasRole('PHARMACIST') and @pharmacySecurityService.isPharmacyOwner(#pharmacyId, principal)")
    public ResponseEntity<SubscriptionDTO> renewSubscription(@PathVariable Long pharmacyId) {
        return ResponseEntity.ok(subscriptionService.renewSubscription(pharmacyId));
    }

    @PostMapping("/{pharmacyId}/cancel")
    @PreAuthorize("hasRole('PHARMACIST') and @pharmacySecurityService.isPharmacyOwner(#pharmacyId, principal)")
    public ResponseEntity<Void> cancelSubscription(@PathVariable Long pharmacyId) {
        subscriptionService.cancelSubscription(pharmacyId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/pharmacy/{pharmacyId}")
    @PreAuthorize("hasRole('PHARMACIST') and @pharmacySecurityService.isPharmacyOwner(#pharmacyId, principal)")
    public ResponseEntity<SubscriptionDTO> getSubscriptionByPharmacy(@PathVariable Long pharmacyId) {
        return ResponseEntity.ok(subscriptionService.getSubscriptionByPharmacy(pharmacyId));
    }

    // Endpoint temporaire pour la simulation de paiement (MVP)
    @PostMapping("/{pharmacyId}/simulate-payment")
    @PreAuthorize("hasRole('PHARMACIST') and @pharmacySecurityService.isPharmacyOwner(#pharmacyId, principal)")
    public ResponseEntity<Void> simulatePayment(
            @PathVariable Long pharmacyId,
            @RequestParam Double amount) {
        subscriptionService.simulatePayment(pharmacyId, amount);
        return ResponseEntity.ok().build();
    }
}

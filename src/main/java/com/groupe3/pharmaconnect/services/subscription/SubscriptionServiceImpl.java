package com.groupe3.pharmaconnect.services.subscription;

import com.groupe3.pharmaconnect.dto.SubscriptionDTO;
import com.groupe3.pharmaconnect.entities.Pharmacy;
import com.groupe3.pharmaconnect.entities.Subscription;
import com.groupe3.pharmaconnect.enums.SubscriptionStatus;
import com.groupe3.pharmaconnect.mappers.SubscriptionMapper;
import com.groupe3.pharmaconnect.repositories.PharmacyRepository;
import com.groupe3.pharmaconnect.repositories.SubscriptionRepository;
import com.groupe3.pharmaconnect.services.Notification.NotificationService;
import com.groupe3.pharmaconnect.services.Notification.NotificationServiceImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final PharmacyRepository pharmacyRepository;
    private final SubscriptionMapper subscriptionMapper;
    private final NotificationServiceImpl notificationService;  // À implémenter pour les notifications

    @Override
    public SubscriptionDTO createSubscription(SubscriptionDTO subscriptionDTO) {
        Pharmacy pharmacy = pharmacyRepository.findById(subscriptionDTO.getPharmacyId())
                .orElseThrow(() -> new ResourceNotFoundException("Pharmacy not found"));

        Subscription subscription = subscriptionMapper.toEntity(subscriptionDTO);
        subscription.setStartDate(LocalDateTime.now());
        subscription.setExpiryDate(LocalDateTime.now().plusMonths(1));
        subscription.setStatus(SubscriptionStatus.ACTIVE);

        // Mise à jour du statut de la pharmacie
        pharmacy.setSubscriptionActive(true);
        pharmacy.setSubscriptionExpiry(subscription.getExpiryDate());
        pharmacyRepository.save(pharmacy);

        return subscriptionMapper.toDTO(subscriptionRepository.save(subscription));
    }

    @Override
    public void simulatePayment(Long pharmacyId, Double amount) {
        // Simulation de paiement pour le MVP
        SubscriptionDTO subscriptionDTO = new SubscriptionDTO();
        subscriptionDTO.setPharmacyId(pharmacyId);
        subscriptionDTO.setAmount(amount);
        createSubscription(subscriptionDTO);
    }

    @Scheduled(cron = "0 0 0 * * *") // Vérifie chaque jour à minuit
    @Override
    public void checkAndUpdateSubscriptionStatus() {
        LocalDateTime now = LocalDateTime.now();
        List<Subscription> expiredSubscriptions = subscriptionRepository
                .findByStatusAndExpiryDateBefore(SubscriptionStatus.ACTIVE, now);

        for (Subscription subscription : expiredSubscriptions) {
            deactivateSubscription(subscription);
            notificationService.sendSubscriptionExpiredNotification(subscription.getPharmacy());
        }

        // Notification pour les abonnements qui expirent bientôt (dans 7 jours)
        List<Subscription> nearingExpiration = subscriptionRepository
                .findByStatusAndExpiryDateBetween(
                        SubscriptionStatus.ACTIVE,
                        now,
                        now.plusDays(7)
                );

        for (Subscription subscription : nearingExpiration) {
            notificationService.sendSubscriptionExpiringNotification(subscription.getPharmacy());
        }
    }

    private void deactivateSubscription(Subscription subscription) {
        subscription.setStatus(SubscriptionStatus.EXPIRED);
        Pharmacy pharmacy = subscription.getPharmacy();
        pharmacy.setSubscriptionActive(false);
        pharmacyRepository.save(pharmacy);
        subscriptionRepository.save(subscription);
    }
}
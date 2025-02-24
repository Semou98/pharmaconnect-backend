package com.groupe3.pharmaconnect.services.subscription;

import com.groupe3.pharmaconnect.dto.SubscriptionDTO;
import com.groupe3.pharmaconnect.entities.Pharmacy;
import com.groupe3.pharmaconnect.entities.Subscription;
import com.groupe3.pharmaconnect.enums.SubscriptionStatus;
import com.groupe3.pharmaconnect.exceptions.BusinessException;
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
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final PharmacyRepository pharmacyRepository;
    private final SubscriptionMapper subscriptionMapper;
    private final NotificationService notificationService;

    @Override
    public SubscriptionDTO createSubscription(SubscriptionDTO subscriptionDTO) {
        Pharmacy pharmacy = pharmacyRepository.findById(subscriptionDTO.getPharmacyId())
                .orElseThrow(() -> new ResourceNotFoundException("Pharmacy not found"));

        // Vérifier s'il existe déjà un abonnement actif
        subscriptionRepository.findByPharmacyIdAndStatus(pharmacy.getId(), SubscriptionStatus.ACTIVE)
                .ifPresent(s -> {
                    throw new BusinessException("An active subscription already exists for this pharmacy");
                });

        Subscription subscription = subscriptionMapper.toEntity(subscriptionDTO);
        subscription.setStartDate(LocalDateTime.now());
        subscription.setExpiryDate(LocalDateTime.now().plusMonths(1));
        subscription.setStatus(SubscriptionStatus.ACTIVE);
        subscription.setPharmacy(pharmacy);

        // Mise à jour du statut de la pharmacie
        pharmacy.setSubscriptionActive(true);
        pharmacy.setSubscriptionExpiry(subscription.getExpiryDate());
        pharmacyRepository.save(pharmacy);

        Subscription savedSubscription = subscriptionRepository.save(subscription);
        notificationService.sendSubscriptionCreatedNotification(pharmacy);

        return subscriptionMapper.toDTO(savedSubscription);
    }

    @Override
    public SubscriptionDTO renewSubscription(Long pharmacyId) {
        Pharmacy pharmacy = pharmacyRepository.findById(pharmacyId)
                .orElseThrow(() -> new ResourceNotFoundException("Pharmacy not found"));

        // Trouver l'abonnement le plus récent
        Subscription lastSubscription = subscriptionRepository
                .findTopByPharmacyOrderByExpiryDateDesc(pharmacy)
                .orElseThrow(() -> new BusinessException("No previous subscription found"));

        // Créer un nouvel abonnement
        Subscription newSubscription = new Subscription();
        newSubscription.setPharmacy(pharmacy);
        newSubscription.setAmount(lastSubscription.getAmount());
        newSubscription.setStartDate(LocalDateTime.now());
        newSubscription.setExpiryDate(LocalDateTime.now().plusMonths(1));
        newSubscription.setStatus(SubscriptionStatus.ACTIVE);

        // Désactiver l'ancien abonnement s'il est encore actif
        if (lastSubscription.getStatus() == SubscriptionStatus.ACTIVE) {
            lastSubscription.setStatus(SubscriptionStatus.EXPIRED);
            subscriptionRepository.save(lastSubscription);
        }

        // Mettre à jour le statut de la pharmacie
        pharmacy.setSubscriptionActive(true);
        pharmacy.setSubscriptionExpiry(newSubscription.getExpiryDate());
        pharmacyRepository.save(pharmacy);

        Subscription savedSubscription = subscriptionRepository.save(newSubscription);
        notificationService.sendSubscriptionRenewedNotification(pharmacy);

        return subscriptionMapper.toDTO(savedSubscription);
    }

    @Override
    public void cancelSubscription(Long pharmacyId) {
        Pharmacy pharmacy = pharmacyRepository.findById(pharmacyId)
                .orElseThrow(() -> new ResourceNotFoundException("Pharmacy not found"));

        Subscription activeSubscription = subscriptionRepository
                .findByPharmacyIdAndStatus(pharmacyId, SubscriptionStatus.ACTIVE)
                .orElseThrow(() -> new BusinessException("No active subscription found"));

        // Mettre à jour le statut de l'abonnement
        activeSubscription.setStatus(SubscriptionStatus.CANCELLED);
        subscriptionRepository.save(activeSubscription);

        // Mettre à jour le statut de la pharmacie
        pharmacy.setSubscriptionActive(false);
        pharmacy.setSubscriptionExpiry(null);
        pharmacyRepository.save(pharmacy);

        notificationService.sendSubscriptionCancelledNotification(pharmacy);
    }

    @Override
    public SubscriptionDTO getSubscriptionByPharmacy(Long pharmacyId) {
        return subscriptionRepository
                .findByPharmacyIdAndStatus(pharmacyId, SubscriptionStatus.ACTIVE)
                .map(subscriptionMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("No active subscription found for this pharmacy"));
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
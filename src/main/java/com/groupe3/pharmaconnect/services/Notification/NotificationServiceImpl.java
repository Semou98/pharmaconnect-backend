package com.groupe3.pharmaconnect.services.Notification;

import com.groupe3.pharmaconnect.entities.Medicament;
import com.groupe3.pharmaconnect.entities.Pharmacy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    @Override
    public void sendSubscriptionExpiredNotification(Pharmacy pharmacy) {
        // Pour le MVP, on log simplement les notifications
        log.info("Notification - Abonnement expiré pour la pharmacie: {} (ID: {})",
                pharmacy.getName(), pharmacy.getId());

        // TODO: Implémenter l'envoi réel de notifications (email, SMS, etc.)
    }

    @Override
    public void sendSubscriptionExpiringNotification(Pharmacy pharmacy) {
        log.info("Notification - Abonnement proche de l'expiration pour la pharmacie: {} (ID: {})",
                pharmacy.getName(), pharmacy.getId());

        // TODO: Implémenter l'envoi réel de notifications (email, SMS, etc.)
    }

    @Override
    public void sendLowStockNotification(Pharmacy pharmacy, Medicament medicament) {
        log.info("Notification - Stock faible pour le médicament: {} dans la pharmacie: {} (ID: {})",
                medicament.getName(), pharmacy.getName(), pharmacy.getId());

        // TODO: Implémenter l'envoi réel de notifications (email, SMS, etc.)
    }

    @Override
    public void sendSubscriptionCreatedNotification(Pharmacy pharmacy) {
        log.info("Notification - Nouvel abonnement créé pour la pharmacie: {} (ID: {})",
                pharmacy.getName(), pharmacy.getId());

        // TODO: Implémenter l'envoi réel de notifications (email, SMS, etc.)
    }

    @Override
    public void sendSubscriptionRenewedNotification(Pharmacy pharmacy) {
        log.info("Notification - Abonnement renouvelé pour la pharmacie: {} (ID: {})",
                pharmacy.getName(), pharmacy.getId());

        // TODO: Implémenter l'envoi réel de notifications (email, SMS, etc.)
    }

    @Override
    public void sendSubscriptionCancelledNotification(Pharmacy pharmacy) {
        log.info("Notification - Abonnement annulé pour la pharmacie: {} (ID: {})",
                pharmacy.getName(), pharmacy.getId());

        // TODO: Implémenter l'envoi réel de notifications (email, SMS, etc.)
    }
}

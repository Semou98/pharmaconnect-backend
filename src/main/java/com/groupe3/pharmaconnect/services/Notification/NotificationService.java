package com.groupe3.pharmaconnect.services.Notification;

import com.groupe3.pharmaconnect.entities.Medicament;
import com.groupe3.pharmaconnect.entities.Pharmacy;

public interface NotificationService {
    void sendSubscriptionExpiredNotification(Pharmacy pharmacy);
    void sendSubscriptionExpiringNotification(Pharmacy pharmacy);
    void sendLowStockNotification(Pharmacy pharmacy, Medicament medicament);
    void sendSubscriptionCreatedNotification(Pharmacy pharmacy);
    void sendSubscriptionRenewedNotification(Pharmacy pharmacy);
    void sendSubscriptionCancelledNotification(Pharmacy pharmacy);
}

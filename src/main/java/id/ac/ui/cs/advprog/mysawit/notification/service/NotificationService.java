package id.ac.ui.cs.advprog.mysawit.notification.service;

import id.ac.ui.cs.advprog.mysawit.notification.dto.NotificationRequest;
import id.ac.ui.cs.advprog.mysawit.notification.dto.NotificationResponse;

import java.util.List;

public interface NotificationService {
    NotificationResponse createNotification(NotificationRequest notificationRequest);
    List<NotificationResponse> getUserNotifications(String userId);
    void markAsRead(String notificationId);
}
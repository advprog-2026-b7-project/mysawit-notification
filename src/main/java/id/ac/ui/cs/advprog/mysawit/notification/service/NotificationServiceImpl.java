package id.ac.ui.cs.advprog.mysawit.notification.service;

import id.ac.ui.cs.advprog.mysawit.notification.dto.NotificationRequest;
import id.ac.ui.cs.advprog.mysawit.notification.dto.NotificationResponse;
import id.ac.ui.cs.advprog.mysawit.notification.entity.Notification;
import id.ac.ui.cs.advprog.mysawit.notification.entity.NotificationType;
import id.ac.ui.cs.advprog.mysawit.notification.mapper.NotificationMapper;
import id.ac.ui.cs.advprog.mysawit.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService{

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    @Override
    public NotificationResponse createNotification(NotificationRequest notificationRequest){
        Notification notification = Notification.builder()
                .userId(notificationRequest.getUserId())
                .title(notificationRequest.getTitle())
                .message(notificationRequest.getMessage())
                .notificationType(NotificationType.valueOf(notificationRequest.getType()))
                .isRead(false)
                .build();

        Notification savedNotification = notificationRepository.save(notification);
        return notificationMapper.convertToResponse(savedNotification);
    }

    @Override
    public List<NotificationResponse> getUserNotifications(String userId){
        List<Notification> notifications =
                notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);

        return notifications.stream()
                .map(notificationMapper::convertToResponse)
                .toList();
    }

    @Override
    public void markAsRead(String notificationId){
        Notification notification = notificationRepository.findById(UUID.fromString(notificationId))
                .orElseThrow(() -> new RuntimeException("Notifikasi tidak ditemukan"));
        notification.setRead(true);
        notificationRepository.save(notification);
    }
}

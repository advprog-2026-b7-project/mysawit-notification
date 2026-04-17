package id.ac.ui.cs.advprog.mysawit.notification.mapper;

import id.ac.ui.cs.advprog.mysawit.notification.dto.NotificationResponse;
import id.ac.ui.cs.advprog.mysawit.notification.entity.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {
    public NotificationResponse convertToResponse(Notification entity) {
        return NotificationResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .message(entity.getMessage())
                .type(entity.getNotificationType().name())
                .isRead(entity.isRead())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}

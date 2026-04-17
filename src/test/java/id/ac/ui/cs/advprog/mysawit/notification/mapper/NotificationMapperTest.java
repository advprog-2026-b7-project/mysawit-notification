package id.ac.ui.cs.advprog.mysawit.notification.mapper;

import id.ac.ui.cs.advprog.mysawit.notification.dto.NotificationResponse;
import id.ac.ui.cs.advprog.mysawit.notification.entity.Notification;
import id.ac.ui.cs.advprog.mysawit.notification.entity.NotificationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class NotificationMapperTest {
    private NotificationMapper notificationMapper;
    private Notification dummyNotification;
    private final UUID dummyId = UUID.randomUUID();
    private final LocalDateTime dummyTime = LocalDateTime.now();

    @BeforeEach
    void setUp(){
        notificationMapper = new NotificationMapper();

        dummyNotification = Notification.builder()
                .id(dummyId)
                .title("Info Sawit")
                .message("Harga pupuk naik")
                .notificationType(NotificationType.GENERAL)
                .isRead(true)
                .createdAt(dummyTime)
                .build();
    }

    @Test
    void testConvertToResponse_Success() {
        NotificationResponse response = notificationMapper.convertToResponse(dummyNotification);

        assertNotNull(response);
        assertEquals(dummyId, response.getId());
        assertEquals("Info Sawit", response.getTitle());
        assertEquals("Harga pupuk naik", response.getMessage());
        assertEquals("GENERAL", response.getType());
        assertTrue(response.isRead());
        assertEquals(dummyTime, response.getCreatedAt());
    }

    @Test
    void testConvertToResponse_NullEntity_ThrowsNullPointerException() {
        Notification nullEntity = null;

        assertThrows(NullPointerException.class, () -> {
            notificationMapper.convertToResponse(nullEntity);
        });
    }

    @Test
    void testConvertToResponse_NullType_ThrowsNullPointerException() {
        dummyNotification.setNotificationType(null);

        assertThrows(NullPointerException.class, () -> {
            notificationMapper.convertToResponse(dummyNotification);
        });
    }


}

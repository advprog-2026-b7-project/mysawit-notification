package id.ac.ui.cs.advprog.mysawit.notification.service;

import id.ac.ui.cs.advprog.mysawit.notification.dto.NotificationRequest;
import id.ac.ui.cs.advprog.mysawit.notification.dto.NotificationResponse;
import id.ac.ui.cs.advprog.mysawit.notification.entity.Notification;
import id.ac.ui.cs.advprog.mysawit.notification.entity.NotificationType;
import id.ac.ui.cs.advprog.mysawit.notification.mapper.NotificationMapper;
import id.ac.ui.cs.advprog.mysawit.notification.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private NotificationMapper notificationMapper;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    private Notification dummyNotification;
    private NotificationRequest dummyRequest;
    private NotificationResponse dummyResponse;
    private final String dummyUserId = "user-123";
    private final UUID dummyNotifId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        dummyNotification = Notification.builder()
                .id(dummyNotifId)
                .userId(dummyUserId)
                .title("Test Title")
                .message("Test Message")
                .notificationType(NotificationType.GENERAL)
                .isRead(false)
                .build();

        dummyRequest = new NotificationRequest();
        dummyRequest.setUserId(dummyUserId);
        dummyRequest.setTitle("Test Title");
        dummyRequest.setMessage("Test Message");
        dummyRequest.setType("GENERAL");

        dummyResponse = new NotificationResponse();
        dummyResponse.setId(dummyNotifId);
        dummyResponse.setTitle("Test Title");
        dummyResponse.setMessage("Test Message");
        dummyResponse.setType("GENERAL");
        dummyResponse.setRead(false);
    }

    @Test
    void testCreateNotificationSuccess(){
        when(notificationRepository.save(any(Notification.class))).thenReturn(dummyNotification);
        when(notificationMapper.convertToResponse(any(Notification.class)))
                .thenReturn(dummyResponse);

        NotificationResponse result = notificationService.createNotification(dummyRequest);

        assertNotNull(result);
        assertEquals("Test Title", result.getTitle());

        verify(notificationRepository, times(1)).save(any(Notification.class));
        verify(notificationMapper, times(1)).convertToResponse(any(Notification.class));
    }

    @Test
    void testCreateNotification_DatabaseError_ThrowsException() {
        when(notificationRepository.save(any(Notification.class)))
                .thenThrow(new RuntimeException("Database sedang bermasalah"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            notificationService.createNotification(dummyRequest);
        });

        assertEquals("Database sedang bermasalah", exception.getMessage());

        verify(notificationMapper, never()).convertToResponse(any(Notification.class));
    }

    @Test
    void testCreateNotification_TypeIsNull_ThrowsNullPointerException() {
        dummyRequest.setType(null);

        assertThrows(NullPointerException.class, () -> {
            notificationService.createNotification(dummyRequest);
        });

        verify(notificationRepository, never()).save(any(Notification.class));
    }

    @Test
    void testCreateNotification_RequestIsNull_ThrowsNullPointerException() {
        NotificationRequest nullRequest = null;

        assertThrows(NullPointerException.class, () -> {
            notificationService.createNotification(nullRequest);
        });

        verify(notificationRepository, never()).save(any(Notification.class));
    }

    @Test
    void testCreateNotification_MapperError_ThrowsException() {
        when(notificationRepository.save(any(Notification.class))).thenReturn(dummyNotification);
        when(notificationMapper.convertToResponse(any(Notification.class)))
                .thenThrow(new RuntimeException("Gagal mengkonversi data"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            notificationService.createNotification(dummyRequest);
        });

        assertEquals("Gagal mengkonversi data", exception.getMessage());

        verify(notificationRepository, times(1)).save(any(Notification.class));
        verify(notificationMapper, times(1)).convertToResponse(any(Notification.class));
    }

    @Test
    void testCreateNotification_InvalidType_ThrowsException() {
        dummyRequest.setType("TIPE_ASAL_ASALAN");

        assertThrows(IllegalArgumentException.class, () -> {
            notificationService.createNotification(dummyRequest);
        });

        verify(notificationRepository, never()).save(any());
    }

    @Test
    void testGetUserNotifications_Success() {
        when(notificationRepository.findByUserIdOrderByCreatedAtDesc(dummyUserId))
                .thenReturn(List.of(dummyNotification));
        when(notificationMapper.convertToResponse(any(Notification.class)))
                .thenReturn(dummyResponse);

        List<NotificationResponse> result = notificationService.getUserNotifications(dummyUserId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Title", result.get(0).getTitle());

        verify(notificationRepository, times(1)).findByUserIdOrderByCreatedAtDesc(dummyUserId);
        verify(notificationMapper, times(1)).convertToResponse(any(Notification.class));
    }

    @Test
    void testGetUserNotifications_EmptyList() {
        when(notificationRepository.findByUserIdOrderByCreatedAtDesc(dummyUserId))
                .thenReturn(List.of());

        List<NotificationResponse> result = notificationService.getUserNotifications(dummyUserId);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(notificationMapper, never()).convertToResponse(any(Notification.class));
    }

    @Test
    void testGetUserNotifications_DatabaseError_ThrowsException() {
        when(notificationRepository.findByUserIdOrderByCreatedAtDesc(dummyUserId))
                .thenThrow(new RuntimeException("Koneksi database terputus"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            notificationService.getUserNotifications(dummyUserId);
        });
        assertEquals("Koneksi database terputus", exception.getMessage());

        verify(notificationMapper, never()).convertToResponse(any());
    }

    @Test
    void testGetUserNotifications_MapperError_ThrowsException() {
        when(notificationRepository.findByUserIdOrderByCreatedAtDesc(dummyUserId))
                .thenReturn(List.of(dummyNotification));
        when(notificationMapper.convertToResponse(any(Notification.class)))
                .thenThrow(new RuntimeException("Gagal melakukan mapping data"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            notificationService.getUserNotifications(dummyUserId);
        });

        assertEquals("Gagal melakukan mapping data", exception.getMessage());

        verify(notificationRepository, times(1)).findByUserIdOrderByCreatedAtDesc(dummyUserId);
        verify(notificationMapper, times(1)).convertToResponse(any(Notification.class));
    }

    @Test
    void testMarkAsRead_Success() {
        when(notificationRepository.findById(dummyNotifId))
                .thenReturn(Optional.of(dummyNotification));

        notificationService.markAsRead(dummyNotifId.toString());

        ArgumentCaptor<Notification> notificationCaptor = ArgumentCaptor
                .forClass(Notification.class);
        verify(notificationRepository, times(1))
                .save(notificationCaptor.capture());

        Notification savedNotification = notificationCaptor.getValue();

        assertTrue(savedNotification.isRead());
    }

    @Test
    void testMarkAsRead_NotFound_ThrowsException() {
        String invalidId = UUID.randomUUID().toString();
        when(notificationRepository.findById(UUID.fromString(invalidId)))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            notificationService.markAsRead(invalidId);
        });

        assertEquals("Notifikasi tidak ditemukan", exception.getMessage());

        verify(notificationRepository, never()).save(any(Notification.class));
    }
}



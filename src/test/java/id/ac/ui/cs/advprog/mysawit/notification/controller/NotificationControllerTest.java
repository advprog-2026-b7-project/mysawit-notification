package id.ac.ui.cs.advprog.mysawit.notification.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.mysawit.notification.dto.NotificationRequest;
import id.ac.ui.cs.advprog.mysawit.notification.dto.NotificationResponse;
import id.ac.ui.cs.advprog.mysawit.notification.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationController.class)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NotificationService notificationService;

    private NotificationRequest dummyRequest;
    private NotificationResponse dummyResponse;
    private final String dummyUserId = "user-123";
    private final UUID dummyNotifId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
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
    void testCreateManualNotification_Success() throws Exception {
        when(notificationService.createNotification(any(NotificationRequest.class)))
                .thenReturn(dummyResponse);

        mockMvc.perform(post("/api/notifications/admin/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dummyRequest)))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Title"))
                .andExpect(jsonPath("$.type").value("GENERAL"));

        verify(notificationService, times(1))
                .createNotification(any(NotificationRequest.class));
    }

    @Test
    void testGetNotifications_Success() throws Exception {
        when(notificationService.getUserNotifications(dummyUserId))
                .thenReturn(List.of(dummyResponse));

        mockMvc.perform(get("/api/notifications/{userId}", dummyUserId)
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].title").value("Test Title"))
                .andExpect(jsonPath("$.length()").value(1));

        verify(notificationService, times(1)).getUserNotifications(dummyUserId);
    }

    @Test
    void testMarkAsRead_Success() throws Exception {
        doNothing().when(notificationService).markAsRead(dummyNotifId.toString());

        mockMvc.perform(patch("/api/notifications/{notificationId}/read", dummyNotifId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Notifikasi berhasil ditandai sebagai telah dibaca."));

        verify(notificationService, times(1)).markAsRead(dummyNotifId.toString());
    }

    @Test
    void testCreateManualNotification_ValidationError_Returns400() throws Exception {
        NotificationRequest invalidRequest = new NotificationRequest();
        invalidRequest.setUserId("user-123");

        mockMvc.perform(post("/api/notifications/admin/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))

                .andExpect(status().isBadRequest());

        verify(notificationService, never()).createNotification(any());
    }

    @Test
    void testMarkAsRead_NotFound_Returns500() throws Exception {
        doThrow(new RuntimeException("Notifikasi tidak ditemukan"))
                .when(notificationService).markAsRead(dummyNotifId.toString());

        mockMvc.perform(patch("/api/notifications/{notificationId}/read", dummyNotifId.toString())
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isInternalServerError());

        verify(notificationService, times(1)).markAsRead(dummyNotifId.toString());
    }

    @Test
    void testCreateManualNotification_MalformedJson_Returns400() throws Exception {
        String malformedJson = "{ \"userId\": \"user-123\", \"title\": \"Judul\" ";

        mockMvc.perform(post("/api/notifications/admin/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformedJson))

                .andExpect(status().isBadRequest());

        verify(notificationService, never()).createNotification(any());
    }

    @Test
    void testGetNotifications_ServiceError_Returns500() throws Exception {
        when(notificationService.getUserNotifications(dummyUserId))
                .thenThrow(new RuntimeException("Database sedang gangguan"));

        mockMvc.perform(get("/api/notifications/{userId}", dummyUserId)
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isInternalServerError());

        verify(notificationService, times(1)).getUserNotifications(dummyUserId);
    }
}

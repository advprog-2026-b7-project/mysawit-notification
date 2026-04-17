package id.ac.ui.cs.advprog.mysawit.notification.controller;

import id.ac.ui.cs.advprog.mysawit.notification.dto.NotificationRequest;
import id.ac.ui.cs.advprog.mysawit.notification.dto.NotificationResponse;
import id.ac.ui.cs.advprog.mysawit.notification.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/admin/create")
    public ResponseEntity<NotificationResponse> createManualNotification(
            @Valid @RequestBody NotificationRequest requestDto) {
        NotificationResponse response = notificationService.createNotification(requestDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<NotificationResponse>> getNotifications(
            @PathVariable String userId) {
        List<NotificationResponse> responses = notificationService.getUserNotifications(userId);
        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<String> markAsRead(@PathVariable String notificationId) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity.ok("Notifikasi berhasil ditandai sebagai telah dibaca.");
    }
}

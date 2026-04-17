package id.ac.ui.cs.advprog.mysawit.notification.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {
    @NotBlank(message = "User ID tidak boleh kosong")
    private String userId;

    @NotBlank(message = "Judul tidak boleh kosong")
    private String title;

    @NotBlank(message = "Pesan tidak boleh kosong")
    private String message;

    @NotNull(message = "Tipe notifikasi tidak boleh kosong")
    private String type;
}

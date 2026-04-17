package id.ac.ui.cs.advprog.mysawit.notification.broker;

import org.springframework.stereotype.Component;

@Component
public class NotificationListener {

    /**
     * Ini adalah SKELETON (kerangka).
     * Nanti saat digabungkan, method ini akan otomatis terpanggil
     * setiap kali ada service lain yang mengirim pesan ke antrean ini.
     */

    // Buka komen (uncomment) baris di bawah ini saat RabbitMQ sudah ada di Docker
    // @RabbitListener(queues = "notification_queue")
    public void receiveMessage(String message) {

        // Konversi 'message' (biasanya berupa JSON) menjadi object DTO
        // Buat object Notification Entity baru
        // Simpan ke database menggunakan notificationRepository.save()
    }
}
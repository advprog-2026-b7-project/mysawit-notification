package id.ac.ui.cs.advprog.mysawit.notification.entity;

public enum NotificationType {
    ASSIGNMENT,         // Untuk penugasan Buruh ke Mandor, atau Mandor ke Kebun
    HARVEST_UPDATE,     // Untuk hasil panen yang disetujui/ditolak Mandor
    DELIVERY_UPDATE,    // Untuk status pengiriman (Tiba di tujuan, disetujui/ditolak)
    PAYROLL_UPDATE,     // Untuk payroll yang disetujui/ditolak Admin
    GENERAL             // Untuk notifikasi umum lainnya (misal: pesan blast dari Admin)
}

# MySawit Notification Service

Service ini jalan di port `8084` dan menggunakan database PostgreSQL terpisah khusus notification.

## Menjalankan Dengan Infra Repo

1. Jalankan dulu infra repo (yang menyalakan PostgreSQL via Docker).
2. Pastikan database plantation tersedia, default nama DB: `mysawit_notification`.
3. Jalankan service ini.

## Konfigurasi Database

Service ini sudah membaca konfigurasi datasource dari environment variable, dengan fallback default untuk local:

- `SPRING_DATASOURCE_URL` (default: `jdbc:postgresql://localhost:5435/api/mysawit_notifications`)
- `SPRING_DATASOURCE_USERNAME` (default: `postgres`)
- `SPRING_DATASOURCE_PASSWORD` (default: `postgres`)

Jika infra repo expose PostgreSQL di host dan port berbeda, cukup ubah env var tersebut.

## Menjalankan Service

### Local (tanpa Docker)

```bash
./gradlew bootRun
```

### Via Docker Compose

```bash
docker compose up --build
```

## Catatan Skema Notification

Entity pada service ini sudah disejajarkan dengan skema tim untuk kebutuhan operasional notifikasi. Terdapat satu tabel utama:

1. **Entity `Notification`** (Tabel `notifications`)
   Menyimpan data pesan atau pemberitahuan yang dikirimkan kepada pengguna.
   - `id` (UUID, Primary Key, Otomatis terisi & tidak bisa diubah)
   - `user_id` (String, Wajib diisi)
   - `title` (String, Wajib diisi)
   - `message` (Text, Wajib diisi)
   - `is_read` (Boolean, Default: `false`)
   - `notification_type` (Enum, Disimpan sebagai String)
   - `created_at` (Timestamp, Otomatis terisi saat pertama kali dibuat)

Fitur pada entity ini juga sudah menerapkan *rule* bahwa `id` bersifat tetap (`updatable = false`) dan tidak dapat diubah setelah notifikasi dibuat.
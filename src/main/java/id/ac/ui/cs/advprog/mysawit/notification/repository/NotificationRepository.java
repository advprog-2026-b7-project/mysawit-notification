package id.ac.ui.cs.advprog.mysawit.notification.repository;

import id.ac.ui.cs.advprog.mysawit.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    List<Notification> findByUserIdOrderByCreatedAtDesc(String userId);
    List<Notification> findByUserIdAndIsReadFalseOrderByCreatedAtDesc(String userId);
    long countByUserIdAndIsReadFalse(String userId);
    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true " +
            "WHERE n.userId = :userId AND n.isRead = false")
    void markAllAsReadByUserId(@Param("userId") String userId);
}

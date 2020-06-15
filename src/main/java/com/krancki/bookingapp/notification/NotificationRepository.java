package com.krancki.bookingapp.notification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByNickNameOrderByCreatedTimeAsc(String nickName);

    @Query("SELECT no FROM Notification no " +
            "WHERE no.nickName = :nickName " +
            "AND no.readByUser = :isRead " +
            "AND no.notificationDeliveryDate <= :currentDate " +
            "ORDER BY no.createdTime ASC")
    List<Notification> findNewByNickName(String nickName, boolean isRead, LocalDate currentDate);
}

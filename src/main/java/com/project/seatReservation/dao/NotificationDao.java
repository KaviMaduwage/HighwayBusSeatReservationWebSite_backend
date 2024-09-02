package com.project.seatReservation.dao;

import com.project.seatReservation.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationDao extends JpaRepository<Notification,Integer> {

    @Query("SELECT n FROM Notification n WHERE n.user.userId = :userId ORDER BY n.notificationId ASC")
    List<Notification> findNotificationsByUserId(int userId);
}

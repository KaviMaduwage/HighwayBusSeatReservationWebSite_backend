package com.project.seatReservation.service;

import com.project.seatReservation.model.Notification;

import java.util.List;

public interface NotificationService {
    void saveNotification(Notification notification);

    List<Notification> findNotificationsByUserId(int userId);
}

package com.project.seatReservation.service;

import com.project.seatReservation.dao.NotificationDao;
import com.project.seatReservation.model.Notification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService{
    NotificationDao notificationDao;

    public NotificationServiceImpl(NotificationDao notificationDao) {
        this.notificationDao = notificationDao;
    }

    @Override
    public void saveNotification(Notification notification) {
        notificationDao.save(notification);
    }

    @Override
    public List<Notification> findNotificationsByUserId(int userId) {
        return notificationDao.findNotificationsByUserId(userId);
    }
}

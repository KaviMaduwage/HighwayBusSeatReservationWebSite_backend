package com.project.seatReservation.controller;

import com.project.seatReservation.model.Notification;
import com.project.seatReservation.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@CrossOrigin
public class NotificationController {
    NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @RequestMapping(value = "/loadNotificationsByUserId", method = RequestMethod.POST)
    public ResponseEntity<?> loadNotificationsByUserId(@RequestBody Map<String, Integer> requestBody){
        int userId = requestBody.get("userId");

        List<Notification> notifications = new ArrayList<>();
        notifications = notificationService.findNotificationsByUserId(userId);


        return ResponseEntity.ok().body(notifications);
    }
}

package com.project.seatReservation.service;

import org.springframework.mail.SimpleMailMessage;

public interface EmailService {

    String sendVerificationEmail(String toAddress, String fromAddress, String senderName, String subject, String content);
}

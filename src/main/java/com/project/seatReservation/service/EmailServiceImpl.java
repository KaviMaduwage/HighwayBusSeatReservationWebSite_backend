package com.project.seatReservation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
public class EmailServiceImpl implements EmailService{

    @Autowired
    private JavaMailSender javaMailSender;


    @Override
    public String sendVerificationEmail(String toAddress, String fromAddress, String senderName, String subject, String content)
    {
        try{
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message);

            mimeMessageHelper.setFrom(fromAddress);
            mimeMessageHelper.setTo(toAddress);
            mimeMessageHelper.setSubject(subject);

            mimeMessageHelper.setText(content,true);

            javaMailSender.send(message);

            return "mail sent. Login to email to verify the email";

        }catch (Exception e){
            throw new RuntimeException("Error sending email: " + e.getMessage(), e);
        }

    }

    @Override
    public void sendEmails(String toAddress, String fromAddress, String senderName, String subject, String content) {
        try{
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message);

            mimeMessageHelper.setFrom(fromAddress);
            mimeMessageHelper.setTo(toAddress);
            mimeMessageHelper.setSubject(subject);

            mimeMessageHelper.setText(content,true);

            javaMailSender.send(message);


        }catch (Exception e){
            throw new RuntimeException("Error sending email: " + e.getMessage(), e);
        }
    }
}

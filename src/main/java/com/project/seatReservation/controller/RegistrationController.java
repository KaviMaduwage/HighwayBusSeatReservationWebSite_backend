package com.project.seatReservation.controller;

import com.project.seatReservation.model.User;
import com.project.seatReservation.service.EmailService;
import com.project.seatReservation.service.UserService;
import net.bytebuddy.utility.RandomString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Controller
@CrossOrigin
public class RegistrationController {
    UserService userService;

    JavaMailSender javaMailSender;

    EmailService emailService;

    public RegistrationController(UserService userService,  EmailService emailService, JavaMailSender javaMailSender) {
        this.userService = userService;

        this.emailService = emailService;
    }

    @RequestMapping(value = "/signUp", method = RequestMethod.POST)
    public ResponseEntity<String> registerUser(@RequestBody User user, HttpServletRequest request)
            throws UnsupportedEncodingException, MessagingException {

        String message ="";

        String email = user.getEmail();

        List<User> userList = userService.findUsersByEmail(email);

        if(userList == null || userList.isEmpty()){

            String randomCode = RandomString.make(64);
            user.setEmailVerified(false);
            user.setVerificationCode(randomCode);

            userService.saveUser(user);

            String siteURL = request.getRequestURL().toString();
            siteURL = siteURL.replace(request.getServletPath(), "");


            String toAddress = user.getEmail();
            String fromAddress = "myseatofficial@gmail.com";
            String senderName = "My Seat";
            String subject = "Please verify your registration";
            String content = "To confirm your account, please click here : "
                    +"http://localhost:8080/confirm-account?token="+user.getVerificationCode();


            message = emailService.sendVerificationEmail(toAddress, fromAddress,senderName,subject,content);

            }else{
                message = "Email already exists.";
            }

            return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @RequestMapping(value="/confirm-account", method= {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<String> confirmUserAccount(@RequestParam("token")String confirmationToken) {

        String message = userService.confirmEmail(confirmationToken);

        if(message.equalsIgnoreCase("Email verified successfully!")){

            return ResponseEntity.status(HttpStatus.FOUND)
                    .header("Location", "http://localhost:3000/signIn")
                    .body(message);
        }else{
            return ResponseEntity.ok(message);
        }

    }


}

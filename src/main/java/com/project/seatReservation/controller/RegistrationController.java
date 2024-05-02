package com.project.seatReservation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.seatReservation.model.BusOwner;
import com.project.seatReservation.model.Passenger;
import com.project.seatReservation.model.Request;
import com.project.seatReservation.model.User;
import com.project.seatReservation.service.*;
import net.bytebuddy.utility.RandomString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@CrossOrigin
public class RegistrationController {
    UserService userService;

    BusOwnerService busOwnerService;

    RequestService requestService;

    JavaMailSender javaMailSender;

    EmailService emailService;
    PassengerService passengerService;

    public RegistrationController(UserService userService,  EmailService emailService,
                                  JavaMailSender javaMailSender, BusOwnerService busOwnerService, RequestService requestService,
                                  PassengerService passengerService) {
        this.userService = userService;

        this.emailService = emailService;
        this.busOwnerService = busOwnerService;
        this.requestService = requestService;
        this.passengerService = passengerService;
    }

    @RequestMapping(value = "/signUp", method = RequestMethod.POST)
    public ResponseEntity<String> registerUser(@RequestBody Map<String,Object> userData, HttpServletRequest request)
            throws UnsupportedEncodingException, MessagingException {

        String message ="";
        ObjectMapper objectMapper = new ObjectMapper();

        User user = objectMapper.convertValue(userData.get("user"), User.class);


        String email = user.getEmail();

        List<User> userList = userService.findUsersByEmail(email);

        if(userList == null || userList.isEmpty()){

            if(user != null && user.getUserType().getUserTypeId() == 2){
                BusOwner busOwner = objectMapper.convertValue(userData.get("busOwner"), BusOwner.class);
                message = registerBusOwner(user,busOwner);
            }else {

                String randomCode = RandomString.make(64);
                user.setEmailVerified(false);
                user.setVerificationCode(randomCode);

                User savedUser = userService.saveUser(user);

                String siteURL = request.getRequestURL().toString();
                siteURL = siteURL.replace(request.getServletPath(), "");


                String toAddress = user.getEmail();
                String fromAddress = "myseatofficial@gmail.com";
                String senderName = "My Seat";
                String subject = "Please verify your registration";
                String content = "To confirm your account, please click here : "
                        + "http://localhost:8080/confirm-account?token=" + user.getVerificationCode()+ "&userId=" + savedUser.getUserId();


                message = emailService.sendVerificationEmail(toAddress, fromAddress, senderName, subject, content);
            }
        }else{
                message = "Email already exists.";
        }

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    private String registerBusOwner(User user, BusOwner busOwner) {
        String message = "";

        user.setEmailVerified(true);
        User savedUser = userService.saveUser(user);

        busOwner.setUser(savedUser);

        busOwnerService.saveBusOwner(busOwner);

        Request request = new Request();
        request.setBusOwner(busOwner);
        request.setRequestedDate(new Date());

        Request savedRequest = requestService.saveRequest(request);

        if(savedRequest != null){
            message = "Request is sent to admin. After admin's confirmation, your account will be activated";
        }else{
            message = "Something is wrong";
        }

        return message;
    }

    @RequestMapping(value="/confirm-account", method= {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<String> confirmUserAccount(@RequestParam("token")String confirmationToken, @RequestParam("userId")int userId) {

        String message = userService.confirmEmail(confirmationToken);

        if(message.equalsIgnoreCase("Email verified successfully!")){
            User user  = userService.findUserByUserId(userId);
            Passenger passenger = new Passenger();
            passenger.setUser(user);
            passenger.setName(user.getUserName());

            passengerService.savePassenger(passenger);

            return ResponseEntity.status(HttpStatus.FOUND)
                    .header("Location", "http://localhost:3000/signIn")
                    .body(message);
        }else{
            return ResponseEntity.ok(message);
        }

    }


}

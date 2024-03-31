package com.project.seatReservation.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.seatReservation.model.Request;
import com.project.seatReservation.model.User;
import com.project.seatReservation.service.EmailService;
import com.project.seatReservation.service.RequestService;
import com.project.seatReservation.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@CrossOrigin
public class RequestController {

    RequestService requestService;
    UserService userService;

    EmailService emailService;

    public RequestController(RequestService requestService, EmailService emailService, UserService userService) {
        this.requestService = requestService;
        this.emailService = emailService;
        this.userService = userService;
    }
    @RequestMapping(value = "/request", method = RequestMethod.GET)
    public ResponseEntity<?> getAllRequests(HttpServletRequest request){
        List<Request> requestList = new ArrayList<>();
        requestList = requestService.getAllRequests();

        return ResponseEntity.ok().body(requestList.toArray());
    }

    @RequestMapping(value = "/rejectRequest", method = RequestMethod.POST)
    public ResponseEntity<String> rejectRequest( @RequestBody Map<String, Object> requestBody){
        String message = "";

        int requestId = (int) requestBody.get("requestId");

        Request requestObj = requestService.findRequestById(requestId);

        if(requestObj != null){
            requestObj.setReject(true);
            requestService.updateRequest(requestObj);

            message = "Request was rejected successfully";

        }else{
            message = "No request for ID "+requestId;
        }

        return ResponseEntity.ok().body(message);
    }

    @RequestMapping(value = "/acceptRequest", method = RequestMethod.POST)
    public ResponseEntity<String> acceptRequest(@RequestBody Map<String, Object> requestBody){
        String message = "";

        int requestId = (int) requestBody.get("requestId");

        Request requestObj = requestService.findRequestById(requestId);

        if(requestObj != null){
            requestObj.setApproved(true);
            requestObj.setAcceptedDate(new Date());
            requestService.updateRequest(requestObj);

            sendEmail(requestObj);

            message = "Request was accepted successfully and email is sent";

        }else{
            message = "No request for ID "+requestId;
        }

        return ResponseEntity.ok().body(message);
    }

    private void sendEmail(Request requestObj) {

        User user = requestObj.getBusOwner().getUser();
        if(user != null){
            user.setActive(true);
            userService.updateUser(user);
        }
        String toAddress = requestObj.getBusOwner().getUser().getEmail();
        String fromAddress = "myseatofficial@gmail.com";
        String senderName = "My Seat";
        String subject = "My Seat Account Activation";
        String content = "Your account is activated. You can now login to the system by using previously defined username and password. Thank you for using My SEAT.";


        emailService.sendEmails(toAddress, fromAddress, senderName, subject, content);
    }
}

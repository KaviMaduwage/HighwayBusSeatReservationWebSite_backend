package com.project.seatReservation.controller;

import com.project.seatReservation.model.User;
import com.project.seatReservation.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@CrossOrigin
public class LoginController {

    UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> registerUser(@RequestBody User user, HttpServletRequest request, HttpSession session){
        boolean isSuccess = userService.isLoginSuccess(user);

        if (isSuccess){
            List<User> user1 =  userService.findUsersByEmail(user.getEmail());

            session.setAttribute("userLogin", user1);
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("userTypeId", user1.get(0).getUserType().getUserTypeId());
            responseData.put("userName", user1.get(0).getUserName());
            responseData.put("userId", user1.get(0).getUserId());

           return ResponseEntity.ok().body(responseData);

        }else{
            return ResponseEntity.ok().body("Invalid");
        }
    }
    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> requestBody){
        String message = "";

        String currentPassword = requestBody.get("currentPassword");
        String newPassword = requestBody.get("newPassword");
        String userId = requestBody.get("userId");

        User user = userService.findUserByUserId(Integer.valueOf(userId));
        Map<String,String> responseData = new HashMap<>();

        if(user != null){
            message = userService.changePassword(user,currentPassword, newPassword);
            if(message.contains("success")){
                responseData.put("status","success");
            }
        }else{
            responseData.put("status","fail");
            message = "No user";
        }

        responseData.put("message", message);


        return ResponseEntity.ok().body(responseData);
    }
}

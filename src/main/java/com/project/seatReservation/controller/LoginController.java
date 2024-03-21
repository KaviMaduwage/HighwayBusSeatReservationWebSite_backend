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
import java.util.List;

@Controller
@CrossOrigin
public class LoginController {

    UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<String> registerUser(@RequestBody User user, HttpServletRequest request, HttpSession session){
        boolean isSuccess = userService.isLoginSuccess(user);

        if (isSuccess){
            List<User> user1 =  userService.findUsersByEmail(user.getEmail());
            if(user1.get(0).getUserType().getUserTypeId() == 1){
                return ResponseEntity.ok().body("/admin-dashboard");
            }else if(user1.get(0).getUserType().getUserTypeId() == 3){
                return ResponseEntity.ok().body("/passenger-dashboard");
            }
           return ResponseEntity.ok().body("success");

        }else{
            return ResponseEntity.ok().body("Invalid");
        }
    }
}

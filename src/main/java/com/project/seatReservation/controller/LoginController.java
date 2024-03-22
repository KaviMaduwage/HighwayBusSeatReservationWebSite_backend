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

           return ResponseEntity.ok().body(responseData);

        }else{
            return ResponseEntity.ok().body("Invalid");
        }
    }
}

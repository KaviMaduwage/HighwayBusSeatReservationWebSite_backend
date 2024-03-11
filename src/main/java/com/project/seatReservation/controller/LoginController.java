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
        boolean isSuccess = false;

        List<User> savedUser = userService.findUsersByEmail(user.getEmail());

        if(!savedUser.isEmpty()){
            if(user.getPassword().equals(savedUser.get(0).getPassword())){
                session.setAttribute("userLogin",user);
                isSuccess = true;
            }

        }else{
            isSuccess = false;
        }

        if (isSuccess){
           return ResponseEntity.ok().body("success");

        }else{
            return ResponseEntity.ok().body("Invalid email or password");
        }
    }
}

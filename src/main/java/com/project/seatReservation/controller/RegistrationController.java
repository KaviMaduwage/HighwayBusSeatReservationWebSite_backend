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

import java.util.List;

@Controller
@CrossOrigin
public class RegistrationController {
    UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/signUp", method = RequestMethod.POST)
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        // Perform registration logic here
        String errorMessage = "";
        String successMessage = "";

        String email = user.getEmail();
        String userName = user.getUserName();
        String password = user.getPassword();

        List<User> userList = userService.findUsersByEmail(email);

        if(userList == null || userList.isEmpty()){

        }else{
            errorMessage = "Email already exists.";
        }

        // Assuming registration is successful
        successMessage = "User registered successfully.";
        return new ResponseEntity<>(successMessage, HttpStatus.OK);
    }
}

package com.project.seatReservation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class RegistrationController {
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registerUser(){
        String message = "";



        return message;
    }
}

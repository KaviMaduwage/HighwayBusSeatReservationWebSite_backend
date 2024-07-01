package com.project.seatReservation.controller;

import com.project.seatReservation.model.FoundItem;
import com.project.seatReservation.model.LostItem;
import com.project.seatReservation.service.LostFoundService;
import com.project.seatReservation.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@CrossOrigin
public class LostFoundController {

    LostFoundService lostFoundService;

    UserService userService;

    public LostFoundController(LostFoundService lostFoundService, UserService userService) {
        this.lostFoundService = lostFoundService;
        this.userService = userService;
    }

    @RequestMapping(value = "/saveFoundItem",method = RequestMethod.POST)
    public ResponseEntity<?> saveFoundItem(@RequestBody FoundItem foundItem){

        String message = "Successfully saved.";
        lostFoundService.saveFoundItem(foundItem);

        return ResponseEntity.ok().body(message);

    }

    @RequestMapping(value = "/saveLostItem",method = RequestMethod.POST)
    public ResponseEntity<?> saveLostItem(@RequestBody LostItem lostItem){

        String message = "Successfully saved.";
        lostFoundService.saveLostItem(lostItem);

        return ResponseEntity.ok().body(message);

    }
}

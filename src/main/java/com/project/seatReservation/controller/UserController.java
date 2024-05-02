package com.project.seatReservation.controller;

import com.project.seatReservation.model.BusCrew;
import com.project.seatReservation.model.BusOwner;
import com.project.seatReservation.model.Passenger;
import com.project.seatReservation.service.BusCrewService;
import com.project.seatReservation.service.BusOwnerService;
import com.project.seatReservation.service.PassengerService;
import com.project.seatReservation.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

@Controller
@CrossOrigin
public class UserController {
    UserService userService;
    BusCrewService busCrewService;
    PassengerService passengerService;
    BusOwnerService busOwnerService;

    public UserController(UserService userService, BusCrewService busCrewService, PassengerService passengerService, BusOwnerService busOwnerService) {
        this.userService = userService;
        this.busCrewService = busCrewService;
        this.passengerService = passengerService;
        this.busOwnerService = busOwnerService;
    }

    @RequestMapping(value = "/findBusOwnerByUserId", method = RequestMethod.POST)
    public ResponseEntity<?> findBusOwnerByUserId(@RequestBody Map<String,Integer> requestBody){
        int userId = requestBody.get("userId");
        List<BusOwner> busOwnerList = busOwnerService.findBusOwnerByUserId(userId);

        return ResponseEntity.ok().body(busOwnerList.get(0));
    }

    @RequestMapping(value = "/findPassengerByUserId", method = RequestMethod.POST)
    public ResponseEntity<?> findPassengerByUserId(@RequestBody Map<String,Integer> requestBody){
        int userId = requestBody.get("userId");
        List<Passenger> passengers = passengerService.findPassengerByUserId(userId);

        return ResponseEntity.ok().body(passengers.get(0));
    }

    @RequestMapping(value = "/findBusCrewByUserId", method = RequestMethod.POST)
    public ResponseEntity<?> findBusCrewByUserId(@RequestBody Map<String,Integer> requestBody){
        int userId = requestBody.get("userId");
        List<BusCrew> busCrewList = busCrewService.findBusCrewByUserId(userId);

        return ResponseEntity.ok().body(busCrewList.get(0));
    }
}

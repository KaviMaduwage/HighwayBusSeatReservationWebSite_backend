package com.project.seatReservation.controller;

import com.project.seatReservation.model.BusCrew;
import com.project.seatReservation.model.BusOwner;
import com.project.seatReservation.model.Passenger;
import com.project.seatReservation.model.Wallet;
import com.project.seatReservation.service.*;
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

    PaymentService paymentService;

    public UserController(UserService userService, BusCrewService busCrewService, PassengerService passengerService, BusOwnerService busOwnerService,PaymentService paymentService) {
        this.userService = userService;
        this.busCrewService = busCrewService;
        this.passengerService = passengerService;
        this.busOwnerService = busOwnerService;
        this.paymentService = paymentService;
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
    @RequestMapping(value = "/getWalletAmountByUSerId", method = RequestMethod.POST)
    public ResponseEntity<?> getWalletAmountByUSerId(@RequestBody Map<String,Integer> requestBody){
        double walletAmount = 0.0;
        int userId = requestBody.get("userId");

        Passenger passenger = passengerService.findPassengerByUserId(userId).get(0);
        Wallet wallet = paymentService.findWalletByPassengerId(passenger.getPassengerId());

        if(wallet != null){
            walletAmount = wallet.getAmount();
        }

        return ResponseEntity.ok().body(walletAmount);

    }
}

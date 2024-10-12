package com.project.seatReservation.controller;

import com.project.seatReservation.model.*;
import com.project.seatReservation.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@CrossOrigin
public class UserController {
    UserService userService;
    BusCrewService busCrewService;
    PassengerService passengerService;
    BusOwnerService busOwnerService;

    PaymentService paymentService;
    BusService busService;
    ReservationService reservationService;
    ScheduleService scheduleService;

    public UserController(UserService userService, BusCrewService busCrewService, PassengerService passengerService, BusOwnerService busOwnerService,PaymentService paymentService,BusService busService,ReservationService reservationService,ScheduleService scheduleService) {
        this.userService = userService;
        this.busCrewService = busCrewService;
        this.passengerService = passengerService;
        this.busOwnerService = busOwnerService;
        this.paymentService = paymentService;
        this.busService = busService;
        this.reservationService = reservationService;
        this.scheduleService = scheduleService;
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
    @RequestMapping(value = "/updatePassenger",method = RequestMethod.POST)
    public ResponseEntity<?> updatePassenger(@RequestBody Passenger passenger){
        String message = "";

        passengerService.updatePassenger(passenger);

        message = "Successfully updated the passenger details.";
        return ResponseEntity.ok().body(message);
    }

    @RequestMapping(value = "/loadAdminSummaryDataForSummaryPage", method = RequestMethod.POST)
    public ResponseEntity<?> loadAdminSummaryDataForSummaryPage(){
        Map<String,Integer> resultMap = new HashMap<>();


        List<Passenger> passengers = passengerService.findAllPassengers();
        resultMap.put("totalUsers",(passengers != null ? passengers.size() : 0));

        List<Bus> busList = busService.loadAllBusDetails();
        resultMap.put("totalBuses",(busList != null ? busList.size() : 0));

        List<BusOwner> busOwnerList = busOwnerService.findActiveBusOwnerList();
        resultMap.put("totalBusOwners",(busOwnerList != null ? busOwnerList.size() : 0));

        List<SeatReservation> reservationList =reservationService.findAllUpcomingReservations();
        resultMap.put("totalReservations",(reservationList!= null ? reservationList.size() : 0));


        return ResponseEntity.ok().body(resultMap);

    }

    @RequestMapping(value = "/loadBusOwnerSummaryDataForSummaryPage", method = RequestMethod.POST)
    public ResponseEntity<?> loadBusOwnerSummaryDataForSummaryPage(@RequestBody Map<String,Integer> requestBody){
        Map<String,Integer> resultMap = new HashMap<>();

        int busOwnerUserId = requestBody.get("userId");
        List<BusOwner> busOwnerList = busOwnerService.findBusOwnerByUserId(busOwnerUserId);
        int busOwnerId = (busOwnerList.size() > 0 ? busOwnerList.get(0).getBusOwnerId() : 0);


        List<Passenger> passengers = passengerService.findAllPassengers();
        resultMap.put("totalUsers",(passengers != null ? passengers.size() : 0));

        List<Bus> busList = busService.loadAllBusDetailsInTravelService(busOwnerId);
        resultMap.put("totalBuses",(busList != null ? busList.size() : 0));

        List<BusCrew> driverList = busCrewService.getDriverListByBusOwnerId(busOwnerId);
        resultMap.put("totalDrivers",(driverList != null ? driverList.size() : 0));

        List<BusCrew> dconductorList = busCrewService.getConductorListByBusOwnerId(busOwnerId);
        resultMap.put("totalConductors",(dconductorList != null ? dconductorList.size() : 0));

        List<SeatReservation> reservationList =reservationService.findAllUpcomingReservationsByTravelService(busOwnerId);
        resultMap.put("totalReservations",(reservationList!= null ? reservationList.size() : 0));


        return ResponseEntity.ok().body(resultMap);

    }

    @RequestMapping(value = "/loadBusCrewSummaryDataForSummaryPage",method = RequestMethod.POST)
    public ResponseEntity<?> loadBusCrewSummaryDataForSummaryPage(@RequestBody Map<String,Integer> requestBody){
        Map<String,Integer> resultMap = new HashMap<>();
        int userId = requestBody.get("userId");

        List<BusCrew> busCrewList = busCrewService.findBusCrewByUserId(userId);
        List<Schedule> todayScheduleList = new ArrayList<>();
        List<Schedule> tomorrowScheduleList = new ArrayList<>();

        if (!busCrewList.isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String today = sdf.format(new Date());

            BusCrew busCrew = busCrewList.get(0);
            todayScheduleList = scheduleService.findBusCrewTodaySchedule(today, busCrew.getBusCrewId());

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, 1);  // Add 1 day to get tomorrow's date
            String tomorrow = sdf.format(calendar.getTime());

            tomorrowScheduleList = scheduleService.findBusCrewTodaySchedule(tomorrow, busCrew.getBusCrewId());
        }



        resultMap.put("todayTrips",todayScheduleList.size());
        resultMap.put("tomorrowTrips",tomorrowScheduleList.size());

        return ResponseEntity.ok().body(resultMap);
    }

    @RequestMapping(value = "/updateBusOwnerDetails",method = RequestMethod.POST)
    public ResponseEntity<?> updateBusOwnerDetails(@RequestBody BusOwner busOwner){
        String message = "";

        int userId = busOwner.getUser().getUserId();
        String newEmail = busOwner.getUser().getEmail();

        User savedUser = userService.findUserByUserId(userId);
        if(savedUser != null && !newEmail.equals(savedUser.getEmail())){
            savedUser.setEmail(newEmail);
            userService.updateUser(savedUser);
        }

        busOwnerService.updateBusOwner(busOwner);

        message = "Successfully updated the travel service details.";
        return ResponseEntity.ok().body(message);
    }
}

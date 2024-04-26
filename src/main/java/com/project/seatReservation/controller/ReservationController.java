package com.project.seatReservation.controller;

import com.project.seatReservation.model.BlockedSeat;
import com.project.seatReservation.model.Schedule;
import com.project.seatReservation.model.User;
import com.project.seatReservation.service.ReservationService;
import com.project.seatReservation.service.ScheduleService;
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
public class ReservationController {
    UserService userService;
    ScheduleService scheduleService;
    ReservationService reservationService;

    public ReservationController(UserService userService, ScheduleService scheduleService, ReservationService reservationService) {
        this.userService = userService;
        this.scheduleService = scheduleService;
        this.reservationService = reservationService;
    }

    @RequestMapping(value = "/findBlockedSeatsByScheduleId", method = RequestMethod.POST)
    public ResponseEntity<?> findBlockedSeatsByScheduleId(@RequestBody Map<String, Integer> requestBody){
        int scheduleId = requestBody.get("scheduleId");

        List<BlockedSeat> blockedSeats = reservationService.findBlockedSeatsByScheduleId(scheduleId);

        return ResponseEntity.ok().body(blockedSeats.toArray());
    }

    @RequestMapping(value = "/blockSeat", method = RequestMethod.POST)
    public ResponseEntity<?> blockSeat(@RequestBody Map<String, Integer> requestBody){

        int userId = requestBody.get("userId");
        int scheduleId = requestBody.get("scheduleId");
        int rowNo = requestBody.get("row");
        int colNo = requestBody.get("col");

        User user = userService.findUserByUserId(userId);
        List<Schedule> schedule = scheduleService.findScheduleById(scheduleId);

        BlockedSeat blockedSeat = new BlockedSeat();
        blockedSeat.setUser(user);
        blockedSeat.setSchedule(schedule.get(0));
        blockedSeat.setRow(rowNo);
        blockedSeat.setCol(colNo);

        BlockedSeat savedBlockedSeat = reservationService.saveBlockedSeat(blockedSeat);

        List<BlockedSeat> blockedSeats = reservationService.findBlockedSeatsByScheduleId(scheduleId);

        return ResponseEntity.ok().body(blockedSeats.toArray());
    }

    @RequestMapping(value = "/unblockSelectedSeat", method = RequestMethod.POST)
    public ResponseEntity<?> unblockSelectedSeat(@RequestBody Map<String, Integer> requestBody){

        int userId = requestBody.get("userId");
        int scheduleId = requestBody.get("scheduleId");
        int rowNo = requestBody.get("row");
        int colNo = requestBody.get("col");


        BlockedSeat savedBlockedSeat = reservationService.findBlockedSeatByScheduleIdUserIdRowAndCol(scheduleId,rowNo,colNo,userId);
        if(savedBlockedSeat != null){
            reservationService.deleteBlockedSeat(savedBlockedSeat);
        }
        List<BlockedSeat> blockedSeats = reservationService.findBlockedSeatsByScheduleId(scheduleId);

        return ResponseEntity.ok().body(blockedSeats.toArray());
    }

    @RequestMapping(value = "/addReservationToCart",method = RequestMethod.POST)
    public ResponseEntity<String> addReservationToCart(@RequestBody Map<String,String> requestBody){
        String message = "Successfully added to the cart. Proceed with other reservations.";

        int userId = Integer.parseInt(requestBody.get("userId"));
        int scheduleId = Integer.parseInt(requestBody.get("scheduleId"));
        String pickUpPoint = requestBody.get("pickUpPoint");
        String dropOffPoint = requestBody.get("dropOffPoint");


        return ResponseEntity.ok().body(message);
    }


}

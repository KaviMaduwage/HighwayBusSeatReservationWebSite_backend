package com.project.seatReservation.controller;

import com.project.seatReservation.model.Alert;
import com.project.seatReservation.model.AlertType;
import com.project.seatReservation.model.BusCrew;
import com.project.seatReservation.model.Schedule;
import com.project.seatReservation.service.AlertService;
import com.project.seatReservation.service.BusCrewService;
import com.project.seatReservation.service.ReservationService;
import com.project.seatReservation.service.ScheduleService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@CrossOrigin
public class AlertController {

    AlertService alertService;
    ScheduleService scheduleService;
    ReservationService reservationService;
    BusCrewService busCrewService;

    public AlertController(AlertService alertService,ScheduleService scheduleService,ReservationService reservationService,BusCrewService busCrewService) {
        this.alertService = alertService;
        this.scheduleService = scheduleService;
        this.reservationService = reservationService;
        this.busCrewService = busCrewService;
    }

    @RequestMapping(value = "/getAlertTypeList",method = RequestMethod.GET)
    public ResponseEntity<?> getAlertTypeList(){
        List<AlertType> alertTypes = new ArrayList<>();
        alertTypes = alertService.getAlertTypeList();

        return ResponseEntity.ok().body(alertTypes.toArray());
    }

    @RequestMapping(value = "/createAlert",method = RequestMethod.POST)
    public ResponseEntity<?> createAlert(@RequestBody Alert alert){
        String message = "Successfully created the alert";

        alert.setCreatedDate(new Date());
        alertService.saveAlert(alert);

        return ResponseEntity.ok().body(message);
    }

    @RequestMapping(value = "/getAllAlerts", method = RequestMethod.POST)
    public ResponseEntity<?> getAllAlerts(@RequestBody Map<String,Integer> requestBody){
        List<Alert> alerts = new ArrayList<>();

        int userTypeId = requestBody.get("userTypeId");
        int userId = requestBody.get("userId");

        LocalDate ld = LocalDate.now();
        LocalDate pastLocalDate = ld.minusDays(10);
        Date pastDate = Date.from(pastLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        alerts = alertService.getAllAlertsByUserTypeId(userTypeId,userId,pastDate);

        return ResponseEntity.ok().body(alerts.toArray());
    }

    @RequestMapping(value = "/getPassengerCurrentSchedule", method = RequestMethod.POST)
    public ResponseEntity<?> getPassengerCurrentSchedule(@RequestBody Map<String,Integer> requestBody){
        int userTypeId = requestBody.get("userTypeId");
        int userId = requestBody.get("userId");
        List<Schedule> scheduleList = new ArrayList<>();
        Schedule currentSchedule = new Schedule();

        if(userTypeId == 3){
            scheduleList =  reservationService.getTodaysScheduleByUserId(userId);

        }else if(userTypeId == 4 || userTypeId ==5){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String today = sdf.format(new Date());

            List<BusCrew> busCrewList = busCrewService.findBusCrewByUserId(userId);
            BusCrew busCrew  =busCrewList.get(0);

            scheduleList  = scheduleService.findBusCrewTodaySchedule(today,busCrew.getBusCrewId());
        }

        LocalTime now = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");

        for(Schedule s : scheduleList){
            String startTimeStr = s.getTripStartTime();
            String endTimeStr = s.getTripEndTime();

            LocalTime startTime = LocalTime.parse(startTimeStr, formatter);
            LocalTime endTime = LocalTime.parse(endTimeStr, formatter);

            if(now.isAfter(startTime) && now.isBefore(endTime)){
                    currentSchedule = s;
            }
        }



        return ResponseEntity.ok().body(currentSchedule);

    }
    @RequestMapping(value = "/getAlertsByUserId",method = RequestMethod.POST)
    public ResponseEntity<?> getAlertsByUserId(@RequestBody Map<String,Integer> requestBody){
        List<Alert> alerts = new ArrayList<>();

        int userId = requestBody.get("userId");
        alerts = alertService.getAlertsByUserId(userId);

        return ResponseEntity.ok().body(alerts.toArray());
    }

    @RequestMapping(value = "/deleteAlertByAlertId", method = RequestMethod.POST)
    public ResponseEntity<?> deleteAlertByAlertId(@RequestBody Map<String,Integer> requestBody){
        int alertId = requestBody.get("alertId");
        Alert alert = alertService.findAlertById(alertId);

        alertService.deleteAlert(alert);

        String message = "Successfully delete the alert";

        return ResponseEntity.ok().body(message);

    }
}

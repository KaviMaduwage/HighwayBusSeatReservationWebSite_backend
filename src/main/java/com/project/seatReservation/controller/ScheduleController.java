package com.project.seatReservation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.seatReservation.model.BusCrew;
import com.project.seatReservation.model.Reservation;
import com.project.seatReservation.model.Schedule;
import com.project.seatReservation.model.TripCrew;
import com.project.seatReservation.service.BusCrewService;
import com.project.seatReservation.service.ReservationService;
import com.project.seatReservation.service.ScheduleService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@CrossOrigin
public class ScheduleController {

    ScheduleService scheduleService;
    BusCrewService busCrewService;
    ReservationService reservationService;

    public ScheduleController(ScheduleService scheduleService, BusCrewService busCrewService,ReservationService reservationService) {
        this.scheduleService = scheduleService;
        this.busCrewService = busCrewService;
        this.reservationService = reservationService;
    }

    @RequestMapping(value = "/saveSchedule", method = RequestMethod.POST)
    public ResponseEntity<?> saveSchedule(@RequestBody Map<String, Object> requestBody){
        String message = "";

        ObjectMapper objectMapper = new ObjectMapper();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        objectMapper.setDateFormat(dateFormat);

        Schedule schedule = objectMapper.convertValue(requestBody.get("schedule"), Schedule.class);

        Object driverIdObj = requestBody.get("driverId");
        int driverId = 0;
        int conductorId = 0;
        if(driverIdObj instanceof Integer) {
            driverId = (int) driverIdObj;
        }else if(driverIdObj instanceof String){
            driverId = Integer.parseInt((String) driverIdObj);
        }

        Object conductorIdObj = requestBody.get("conductorId");

        if(conductorIdObj instanceof Integer){
            conductorId = (int) conductorIdObj;
        }else if(conductorIdObj instanceof String){
            conductorId = Integer.parseInt((String) conductorIdObj);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String origin = schedule.getOrigin();
        String destination = schedule.getDestination();
        String tripDateStr = schedule.getTripDateStr();
        Date tripDate = null;
        try {
            tripDate = sdf.parse(tripDateStr);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        String startTime = schedule.getTripStartTime();

        List<Schedule> availableSchedules = scheduleService.findScheduleByOriginDestinationAndStartDateTime(tripDate,origin,destination,startTime);

        if(schedule.getScheduleId() == 0 && availableSchedules.isEmpty()){
            //add
            Schedule savedSchedule = scheduleService.saveSchedule(schedule);
            if(conductorId != 0 || driverId != 0){
                saveTripCrew(conductorId,driverId,savedSchedule);
            }

        }else if(schedule.getScheduleId() != 0){
            //update
            Schedule updatedSchedule = scheduleService.updateSchedule(schedule);
            if(conductorId != 0 || driverId != 0){
                List<TripCrew> crewList = scheduleService.getCrewListByScheduleId(updatedSchedule.getScheduleId());
                if(crewList.isEmpty()){
                    saveTripCrew(conductorId,driverId,updatedSchedule);
                }else {
                    boolean isDriverAssigned = false;
                    boolean isConductorAssigned = false;

                    for(TripCrew tripCrew : crewList){

                        if(tripCrew.getBusCrew().getBusCrewType().getDescription() == "Driver"){
                            isDriverAssigned = true;
                            if(driverId != tripCrew.getBusCrew().getBusCrewId()){
                                BusCrew busCrew = busCrewService.findBusCrewById(driverId);
                                tripCrew.setBusCrew(busCrew);
                                scheduleService.updateTripCrew(tripCrew);
                            }

                        }else if(tripCrew.getBusCrew().getBusCrewType().getDescription() == "Conductor"){
                            isConductorAssigned = true;
                            if(conductorId != tripCrew.getBusCrew().getBusCrewId()){
                                BusCrew busCrew = busCrewService.findBusCrewById(conductorId);
                                tripCrew.setBusCrew(busCrew);
                                scheduleService.updateTripCrew(tripCrew);
                            }
                        }
                    }

                    if(!isDriverAssigned){
                        saveTripCrew(0,driverId,updatedSchedule);
                    }

                    if(!isConductorAssigned){
                        saveTripCrew(conductorId,0,updatedSchedule);
                    }


                }

            }

        }else{
            message = "Another trip is schedule on "+schedule.getTripDateStr()+" at "+schedule.getTripStartTime()+" from "+schedule.getOrigin()+" to "+schedule.getDestination();
        }



        return ResponseEntity.ok().body(message);
    }

    private void saveTripCrew(int conductorId, int driverId, Schedule savedSchedule) {
        List<TripCrew> toBeSavedCrewList = new ArrayList<>();
        if(driverId != 0){
            BusCrew busCrew = busCrewService.findBusCrewById(driverId);
            TripCrew tripCrew = new TripCrew();
            tripCrew.setSchedule(savedSchedule);
            tripCrew.setBusCrew(busCrew);

            toBeSavedCrewList.add(tripCrew);
        }

        if(conductorId != 0){
            BusCrew busCrew = busCrewService.findBusCrewById(conductorId);
            TripCrew tripCrew = new TripCrew();
            tripCrew.setSchedule(savedSchedule);
            tripCrew.setBusCrew(busCrew);

            toBeSavedCrewList.add(tripCrew);
        }

        scheduleService.saveTripCrew(toBeSavedCrewList);






    }
    @RequestMapping(value = "/loadScheduleByDate", method = RequestMethod.POST)
    public ResponseEntity<?> loadScheduleByDate(@RequestBody Map<String,String> requestBody){
        List<Schedule> scheduleList = new ArrayList<>();
        String date = requestBody.get("date");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date newDate =null;
        try {

            newDate = sdf.parse(date);

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        scheduleList = scheduleService.findScheduleByDate(newDate);

        List<Map<String, Object>> responseData = new ArrayList<>();

        for(Schedule schedule: scheduleList){
            Map<String, Object> s = new HashMap<>();
            s.put("schedule", schedule);

            int availableSeats = checkSeatAvailabilityByScheduleId(schedule.getScheduleId());
            s.put("seatsAvailable", availableSeats);

            responseData.add(s);
        }




        return ResponseEntity.ok().body(responseData);
    }


    public int checkSeatAvailabilityByScheduleId(int scheduleId){
        int availableSeat = 0;

        List<Schedule> scheduleList = scheduleService.findScheduleById(scheduleId);

        if(scheduleList != null && !scheduleList.isEmpty()){
            Schedule s = scheduleList.get(0);
            int allSeats = s.getBus().getNoOfSeats();
            List<Reservation> reservationList = reservationService.findReservationsByScheduleId(scheduleId);

            availableSeat = allSeats - reservationList.size();
        }


        return availableSeat;
    }
    @RequestMapping(value = "/findBusScheduleByDateTownAndRoute",method = RequestMethod.POST)
    public ResponseEntity<?> findBusScheduleByDateTownAndRoute(@RequestBody Map<String,String> requestBody){
        List<Schedule> scheduleList = new ArrayList<>();
        String dateStr = requestBody.get("date");
        String origin = requestBody.get("origin");
        String destination = requestBody.get("destination");
        String routeIdStr = requestBody.get("routeId");

        int routeId = (routeIdStr.equals("") ? 0 : Integer.parseInt(routeIdStr));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date =null;
        try {

            date = sdf.parse(dateStr);

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        scheduleList = scheduleService.findBusScheduleByDateTownAndRoute(date, origin, destination,routeId);


        List<Map<String, Object>> responseData = new ArrayList<>();

        for(Schedule schedule: scheduleList){
            Map<String, Object> s = new HashMap<>();
            s.put("schedule", schedule);

            int availableSeats = checkSeatAvailabilityByScheduleId(schedule.getScheduleId());
            s.put("seatsAvailable", availableSeats);

            responseData.add(s);
        }



        return ResponseEntity.ok().body(responseData);
    }

    @RequestMapping(value = "/findScheduleById", method = RequestMethod.POST)
    public ResponseEntity<?> findScheduleById(@RequestBody Map<String,Integer> requestBody){
        int scheduleId = requestBody.get("scheduleId");
        List<Schedule> scheduleList = scheduleService.findScheduleById(scheduleId);
        Schedule schedule = new Schedule();
        if(scheduleList != null && !scheduleList.isEmpty()){
            schedule = scheduleList.get(0);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("schedule", schedule);

        List<TripCrew> crewList = scheduleService.getCrewListByScheduleId(scheduleId);
        int driverId = 0;
        int conductorId = 0;
        for(TripCrew tc : crewList){
            if(tc.getBusCrew().getBusCrewType().getDescription().equalsIgnoreCase("Driver")){
                driverId = tc.getBusCrew().getBusCrewId();
            }else if (tc.getBusCrew().getBusCrewType().getDescription().equalsIgnoreCase("Conductor")){
                conductorId = tc.getBusCrew().getBusCrewId();
            }
        }

        result.put("driverId", driverId);
        result.put("conductorId", conductorId);

        return ResponseEntity.ok().body(result);
    }
}

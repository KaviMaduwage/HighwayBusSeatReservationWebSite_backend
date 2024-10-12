package com.project.seatReservation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.seatReservation.model.Bus;
import com.project.seatReservation.model.BusOwner;
import com.project.seatReservation.model.Schedule;
import com.project.seatReservation.model.Seat;
import com.project.seatReservation.service.BusOwnerService;
import com.project.seatReservation.service.BusService;
import com.project.seatReservation.service.ScheduleService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@CrossOrigin
public class BusController {
    BusService busService;
    BusOwnerService busOwnerService;
    ScheduleService scheduleService;

    public BusController(BusService busService, BusOwnerService busOwnerService,ScheduleService scheduleService) {
        this.busService = busService;
        this.busOwnerService = busOwnerService;
        this.scheduleService = scheduleService;
    }

    @RequestMapping(value = "/saveBusDetails", method = RequestMethod.POST)
    public ResponseEntity<String> saveBusDetails(@RequestBody Map<String, Object> requestBody){
        String message = "";

        ObjectMapper objectMapper = new ObjectMapper();
        Bus bus = objectMapper.convertValue(requestBody.get("busDetail"), Bus.class);
        int userId = (int) requestBody.get("userId");

        List<Bus> busList = busService.findBusByPlateNo(bus.getPlateNo());

        List<BusOwner> busOwnerList = busOwnerService.findBusOwnerByUserId(userId);
        bus.setBusOwner(busOwnerList.get(0));

        if(busList != null && busList.isEmpty() && bus.getBusId() == 0){
            //add
            busService.saveBusDetails(bus);
            message = "Successfully saved bus details";


        }else if(busList != null && !busList.isEmpty() && bus.getBusId() != 0){
            //update
            busService.updateBusDetail(bus);
            message = "Successfully updated.";

        }else{
            message = "Already exists.";
        }

        return ResponseEntity.ok().body(message);
    }


    @RequestMapping(value = "/loadBusDetails", method = RequestMethod.POST)
    public ResponseEntity<?> loadBusDetails(){
        List<Bus> busList = new ArrayList<>();
        busList = busService.loadAllBusDetails();

        return ResponseEntity.ok().body(busList.toArray());
    }
    @RequestMapping(value = "/findBusById", method = RequestMethod.POST)
    public ResponseEntity<?> findBusById(@RequestBody Map<String, Integer> requestBody){
        int busId = requestBody.get("busId");
        Bus bus = busService.findBusById(busId);

        return ResponseEntity.ok().body(bus);
    }

    @RequestMapping(value = "/saveSeatStructure", method = RequestMethod.POST)
    public ResponseEntity<String> saveSeatStructure(@RequestBody Map<String, Object> requestBody){
        String message ="";
        List<Map<String,Integer>> selectedSeats = new ArrayList<>();

        selectedSeats = (List<Map<String, Integer>>) requestBody.get("seatStructure");
        List<String> selectedSeatsStrList = new ArrayList<>();
        for(Map<String,Integer> seat : selectedSeats){
            int row = seat.get("row");
            int col = seat.get("col");

            selectedSeatsStrList.add(row+"-"+col);

        }

        int busId = (int) requestBody.get("busId");
        Bus bus = busService.findBusById(busId);

        List<Seat> seatList = new ArrayList<>();



        List<Schedule> futureScheduleList = scheduleService.findFutureSchedulesByDate(new Date());
        List<Seat> alreadyAvailableSeats = busService.findSeatStructureByBusId(busId);

        // find removing seats
        List<Seat> removingSeatList = new ArrayList<>();
        for(Seat s : alreadyAvailableSeats){
            String pattern = s.getRowNo()+"-"+s.getColumnNo();
            if(!selectedSeatsStrList.contains(pattern)){
                Seat removeSeat = new Seat();
                removeSeat.setSeatId(s.getSeatId());
                removeSeat.setColumnNo(s.getColumnNo());
                removeSeat.setRowNo(s.getRowNo());
                removeSeat.setBus(bus);

                removeSeat.setIsLocked(true);
                removingSeatList.add(removeSeat);
            }
        }

        //add new seats
        for(Map<String,Integer> seat : selectedSeats){
            int row = seat.get("row");
            int col = seat.get("col");

            Seat busSeat = new Seat();
            busSeat.setColumnNo(col);
            busSeat.setRowNo(row);
            busSeat.setBus(bus);

            List<Seat> availableSeats = busService.findSeatsByBusIdRowandColNo(busId,row,col);
            if(availableSeats == null || availableSeats.isEmpty()){
                seatList.add(busSeat);
            }else  if(!availableSeats.isEmpty()){
                Seat seat1 = availableSeats.get(0);
                if(seat1.getIsLocked()){
                    seat1.setIsLocked(false);
                    seatList.add(seat1);
                }

            }


        }



        if(seatList.size() > 0){
            busService.saveSeatStructure(seatList);
            message = "Successfully saved the new seats. ";
        }

        if(removingSeatList.size() >0 && futureScheduleList.isEmpty()){
            busService.saveSeatStructure(removingSeatList);
            message += "Successfully removed the selected seats. ";
        }else{
            message += "Can't remove previously selected seats becasue you have future schedules for the selected bus.";
        }

        if(message.equals("")){
            message = "No change in the structure.";
        }


        return ResponseEntity.ok().body(message);
    }
    @RequestMapping(value = "/findSeatStructureByBusId", method = RequestMethod.POST)
    public ResponseEntity<?> findSeatStructureByBusId(@RequestBody Map<String,Integer> requestBody){
        List<Map<String,Integer>> seatStructure = new ArrayList<>();

        int busId = requestBody.get("busId");
        List<Seat> seatList = busService.findSeatStructureByBusId(busId);

        for(Seat seat : seatList){
            Map<String, Integer> selectedSeat = new HashMap<>();
            selectedSeat.put("row", seat.getRowNo());
            selectedSeat.put("col",seat.getColumnNo());

            seatStructure.add(selectedSeat);
        }

        return ResponseEntity.ok().body(seatStructure.toArray());
    }
    @RequestMapping(value = "/loadAllBusDetailsInTravelService", method = RequestMethod.POST)
    public ResponseEntity<?> loadAllBusDetailsInTravelService(@RequestBody Map<String, Integer> requestBody){
        List<Bus> busList = new ArrayList<>();
        int userId = requestBody.get("userId");
        List<BusOwner> busOwnerList = busOwnerService.findBusOwnerByUserId(userId);
        busList = busService.loadAllBusDetailsInTravelService(busOwnerList.get(0).getBusOwnerId());

        return ResponseEntity.ok().body(busList.toArray());
    }

    @RequestMapping(value = "/getAllTravelServiceList", method = RequestMethod.POST)
    public ResponseEntity<?> getAllTravelServiceList(){
        List<BusOwner> busOwnerList = busOwnerService.getAllBusOwners();

        return ResponseEntity.ok().body(busOwnerList.toArray());
    }


}

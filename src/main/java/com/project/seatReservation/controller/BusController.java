package com.project.seatReservation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.seatReservation.model.Bus;
import com.project.seatReservation.model.BusOwner;
import com.project.seatReservation.model.Seat;
import com.project.seatReservation.service.BusOwnerService;
import com.project.seatReservation.service.BusService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@CrossOrigin
public class BusController {
    BusService busService;
    BusOwnerService busOwnerService;

    public BusController(BusService busService, BusOwnerService busOwnerService) {
        this.busService = busService;
        this.busOwnerService = busOwnerService;
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
        int busId = (int) requestBody.get("busId");
        Bus bus = busService.findBusById(busId);

        List<Seat> seatList = new ArrayList<>();

        for(Map<String,Integer> seat : selectedSeats){
            int row = seat.get("row");
            int col = seat.get("col");

            Seat busSeat = new Seat();
            busSeat.setColumnNo(col);
            busSeat.setRowNo(row);
            busSeat.setBus(bus);

            seatList.add(busSeat);

        }

        busService.saveSeatStructure(seatList);
        message = "Successfully saved the seat structure";


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

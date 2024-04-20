package com.project.seatReservation.controller;

import com.project.seatReservation.model.*;
import com.project.seatReservation.service.BusCrewService;
import com.project.seatReservation.service.BusOwnerService;
import com.project.seatReservation.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@CrossOrigin
public class BusCrewController {
    public static  final String GENERATED_PASSWORD = "myseat@2024";
    BusCrewService busCrewService;
    BusOwnerService busOwnerService;
    UserService userService;

    public BusCrewController(BusCrewService busCrewService, UserService userService, BusOwnerService busOwnerService) {
        this.busCrewService = busCrewService;
        this.userService = userService;
        this.busOwnerService = busOwnerService;
    }

    @RequestMapping(value = "/loadBusCrewTypes", method = RequestMethod.GET)
    public ResponseEntity<?> loadBusCrewTypes(){
        List<BusCrewType> busCrewTypes = busCrewService.getBusCrewTypes();

        return ResponseEntity.ok().body(busCrewTypes.toArray());
    }
    @RequestMapping(value = "/saveBusCrew", method = RequestMethod.POST)
    public ResponseEntity<String> saveBusCrew(@RequestBody BusCrew busCrew, HttpServletRequest request, HttpSession session){
        String message = "";

        List<User> emailExists = userService.findUsersByEmail(busCrew.getUser().getEmail());
        if((emailExists == null || emailExists.isEmpty()) && busCrew.getBusCrewId() == 0){

            // add user
            User user = busCrew.getUser();
            user.setActive(true);
            user.setEmailVerified(true);

            String[] userNameArray = busCrew.getName().split(" ");
            String userName = (userNameArray.length > 0 ? userNameArray[0] : "");
            user.setUserName(userName);

            String userTypeDes = busCrew.getBusCrewType().getDescription();
            UserType userType = new UserType();
            userType.setDescription(userTypeDes);

            if(userTypeDes.equalsIgnoreCase("driver")){
                userType.setUserTypeId(4);
            } else if (userTypeDes.equalsIgnoreCase("conductor")) {
                userType.setUserTypeId(5);
            }
            user.setUserType(userType);

            user.setPassword(GENERATED_PASSWORD);

            User savedUser = userService.saveUser(user);

            int busOwnerUserId = busCrew.getBusOwner().getUser().getUserId();

            List<BusOwner> busOwnerList = busOwnerService.findBusOwnerByUserId(busOwnerUserId);
            if(busOwnerList != null && busOwnerList.size() >0){
                busCrew.setBusOwner(busOwnerList.get(0));
            }
            busCrew.setUser(savedUser);
            BusCrew savedBusCrew = busCrewService.saveBusCrew(busCrew);


            if(savedBusCrew != null){
                message = "Successfully saved and created the logins. Password is "+GENERATED_PASSWORD;
            }else{
                message = "Error in saving";
            }
        }else if(busCrew.getBusCrewId() != 0){
            // update
            User updatedUser = new User();
            if(!emailExists.get(0).getEmail().equals(busCrew.getUser().getEmail())){
                updatedUser = userService.saveUser(busCrew.getUser());
            }else{
                updatedUser = emailExists.get(0);
            }

            busCrew.setUser(updatedUser);

            int busOwnerUserId = busCrew.getBusOwner().getUser().getUserId();

            List<BusOwner> busOwnerList = busOwnerService.findBusOwnerByUserId(busOwnerUserId);
            if(busOwnerList != null && busOwnerList.size() >0){
                busCrew.setBusOwner(busOwnerList.get(0));
            }

            busCrewService.updateBusCrew(busCrew);

            message = "Successfully updated";

        }else{
            message = "Email already exists.";
        }

        return ResponseEntity.ok().body(message);
    }
    @RequestMapping(value = "/loadAllBusCrewDetailsByBusOwnerUserId", method = RequestMethod.POST)
    public ResponseEntity<?> loadAllBusCrewDetailsByBusOwnerUserId(@RequestBody User user){
        List<BusCrew> busCrewList = new ArrayList<>();
        List<BusOwner> busOwnerList = busOwnerService.findBusOwnerByUserId(user.getUserId());
        if(busOwnerList!= null && busOwnerList.size() >0){
            busCrewList = busCrewService.getBusCrewByOwnerId(busOwnerList.get(0).getBusOwnerId());
        }
        return ResponseEntity.ok().body(busCrewList.toArray());
    }
    @RequestMapping(value = "/findMemberById", method = RequestMethod.POST)
    public ResponseEntity<?> findMemberById(@RequestBody Map<String, Integer> requestBody){

        int memberId = requestBody.get("memberId");
        BusCrew busCrew = busCrewService.findBusCrewById(memberId);

        return ResponseEntity.ok().body(busCrew);
    }

    @RequestMapping(value = "/deleteMemberById", method = RequestMethod.POST)
    public ResponseEntity<String> deleteMemberById(@RequestBody Map<String, Integer> requestBody){
        String message = "";
        int busCrewId = requestBody.get("memberId");
        BusCrew busCrew = busCrewService.findBusCrewById(busCrewId);

        if(busCrew != null){
            busCrewService.deleteStaffMember(busCrew);

            User user = busCrew.getUser();
            user.setActive(false);
            userService.updateUser(user);
            message = "Successfully deleted";
        }else{
            message = "No member exists.";
        }


        return ResponseEntity.ok().body(message);
    }

    @RequestMapping(value = "/searchMember", method = RequestMethod.POST)
    public ResponseEntity<?> searchBusCrew(@RequestBody Map<String,String> requestData){
        List<BusCrew> busCrewList = new ArrayList<>();
        String namePhrase = requestData.get("name");
        String searchCrewTypeId = requestData.get("jobType");
        String searchStatus = requestData.get("status");
        String userId = requestData.get("userId");
        int busOwnerId = 0;

        List<BusOwner> busOwnerList = busOwnerService.findBusOwnerByUserId(Integer.parseInt(userId));
        if(busOwnerList != null && !busOwnerList.isEmpty()){
            busOwnerId = busOwnerList.get(0).getBusOwnerId();
        }

        busCrewList = busCrewService.findBusCrewByNameJobTypeStatusAndBusOwnerId(namePhrase, Integer.valueOf(searchCrewTypeId), searchStatus, busOwnerId);

        return ResponseEntity.ok().body(busCrewList.toArray());
    }

    @RequestMapping(value = "/loadDriversInTravelService", method = RequestMethod.POST)
    public ResponseEntity<?> loadDriversInTravelService(@RequestBody Map<String, Integer> requestBody){

        int userId = requestBody.get("userId");
        List<BusOwner> busOwnerList = busOwnerService.findBusOwnerByUserId(userId);
        int busOwnerId = busOwnerList.get(0).getBusOwnerId();
        int crewTypeId = 1;
        List<BusCrew> driverList = busCrewService.loadBusCrewByTypeInTravelService(busOwnerId, crewTypeId);

        return ResponseEntity.ok().body(driverList.toArray());
    }

    @RequestMapping(value = "/loadConductorsInTravelService", method = RequestMethod.POST)
    public ResponseEntity<?> loadConductorsInTravelService(@RequestBody Map<String, Integer> requestBody){

        int userId = requestBody.get("userId");
        List<BusOwner> busOwnerList = busOwnerService.findBusOwnerByUserId(userId);
        int busOwnerId = busOwnerList.get(0).getBusOwnerId();
        int crewTypeId = 2;
        List<BusCrew> driverList = busCrewService.loadBusCrewByTypeInTravelService(busOwnerId, crewTypeId);

        return ResponseEntity.ok().body(driverList.toArray());
    }
}

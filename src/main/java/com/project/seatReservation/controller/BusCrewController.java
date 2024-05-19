package com.project.seatReservation.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.seatReservation.model.*;
import com.project.seatReservation.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@CrossOrigin
public class BusCrewController {
    public static  final String GENERATED_PASSWORD = "myseat@2024";
    BusCrewService busCrewService;
    BusOwnerService busOwnerService;
    UserService userService;
    ScheduleService scheduleService;
    ReservationService reservationService;

    public BusCrewController(BusCrewService busCrewService, UserService userService, BusOwnerService busOwnerService,
                             ScheduleService scheduleService,ReservationService reservationService) {
        this.busCrewService = busCrewService;
        this.userService = userService;
        this.busOwnerService = busOwnerService;
        this.scheduleService = scheduleService;
        this.reservationService = reservationService;
    }

    @RequestMapping(value = "/loadBusCrewTypes", method = RequestMethod.GET)
    public ResponseEntity<?> loadBusCrewTypes(){
        List<BusCrewType> busCrewTypes = busCrewService.getBusCrewTypes();

        return ResponseEntity.ok().body(busCrewTypes.toArray());
    }
    @RequestMapping(value = "/saveBusCrew", method = RequestMethod.POST)
    public ResponseEntity<String> saveBusCrew(
                                              @RequestParam("busCrew") String busCrewJson) throws JsonProcessingException,IOException {
        String message = "";

        // Convert JSON string to busCrew object
        ObjectMapper objectMapper = new ObjectMapper();
        BusCrew busCrew = objectMapper.readValue(busCrewJson, BusCrew.class);

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

//            if(profileImage != null && !profileImage.isEmpty()){
//                String fileName = StringUtils.cleanPath(profileImage.getOriginalFilename());
//                String uploadDir = "src/main/resources/images/"+busOwnerList.get(0).getTravelServiceName()+"/busCrew";
//                Path uploadpath = Paths.get(uploadDir);
//
//                try{
//                    if(!Files.exists(uploadpath)){
//                        Files.createDirectories(uploadpath);
//                    }
//                    InputStream inputStream = profileImage.getInputStream();
//                    Path filePath = uploadpath.resolve(fileName);
//
//                    Files.copy(inputStream, filePath,
//                            StandardCopyOption.REPLACE_EXISTING);
//
//                }catch (IOException e){
//                    throw  new IOException("couldn't save uploaded image :"+ fileName);
//                }
//
//            }


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
            List<TripCrew> tripCrewList = busCrewService.findTripsByCrewId(busCrew.getBusCrewId());

            if(!tripCrewList.isEmpty()){
                message = "Can't delete. Has been assigned to a trip.";
            }else{
                busCrewService.deleteStaffMember(busCrew);

                User user = busCrew.getUser();
                user.setActive(false);
                userService.updateUser(user);
                message = "Successfully deleted";
            }

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
    public ResponseEntity<?> loadConductorsInTravelService(@RequestBody Map<String, Integer> requestBody) {

        int userId = requestBody.get("userId");
        List<BusOwner> busOwnerList = busOwnerService.findBusOwnerByUserId(userId);
        int busOwnerId = busOwnerList.get(0).getBusOwnerId();
        int crewTypeId = 2;
        List<BusCrew> driverList = busCrewService.loadBusCrewByTypeInTravelService(busOwnerId, crewTypeId);

        return ResponseEntity.ok().body(driverList.toArray());
    }

    @RequestMapping(value = "/findScheduleByCrewUserId", method = RequestMethod.POST)
    public ResponseEntity<?> findScheduleByCrewUserId(@RequestBody Map<String, Integer> requestBody) {
        List<Schedule> scheduleList = new ArrayList<>();
        List<Map<String,Object>> resultList = new ArrayList<>();

        int userId = requestBody.get("userId");
        List<BusCrew> busCrewList = busCrewService.findBusCrewByUserId(userId);

        if (!busCrewList.isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String today = sdf.format(new Date());

            BusCrew busCrew = busCrewList.get(0);
            scheduleList = scheduleService.findBusCrewTodaySchedule(today, busCrew.getBusCrewId());



            if(scheduleList != null && !scheduleList.isEmpty()){
                for(Schedule schedule : scheduleList){
                    Map<String,Object> scheduleResult = new HashMap<>();

                    int scheduleId = schedule.getScheduleId();
                    BusCrew driver = busCrewService.findDriverByScheduleId(scheduleId);
                    BusCrew conductor = busCrewService.findConductorByScheduleId(scheduleId);

                    String availableSeatsStr = getBookedSeatAmountByScheduleId(schedule.getScheduleId());

                    scheduleResult.put("schedule",schedule);
                    scheduleResult.put("driverName",(driver != null ? driver.getName() : ""));
                    scheduleResult.put("conductorName",(conductor != null ? conductor.getName() : ""));
                    scheduleResult.put("availableSeats",availableSeatsStr);

                    resultList.add(scheduleResult);

                }
            }
        }


        return ResponseEntity.ok().body(resultList.toArray());
    }

    private String getBookedSeatAmountByScheduleId(int scheduleId) {
        int availableSeat = 0;
        String availableSeatsStr = "";

        List<Schedule> scheduleList = scheduleService.findScheduleById(scheduleId);

        if(scheduleList != null && !scheduleList.isEmpty()){
            Schedule s = scheduleList.get(0);
            int allSeats = s.getBus().getNoOfSeats();
            List<Reservation> reservationList = reservationService.findReservationsByScheduleId(scheduleId);

            availableSeat = allSeats - reservationList.size();
            availableSeatsStr = reservationList.size() +"/"+allSeats;
        }


        return availableSeatsStr;
    }

    @RequestMapping(value = "/findDriverByScheduleId", method = RequestMethod.POST)
    public ResponseEntity<?> findDriverByScheduleId(@RequestBody Map<String, Integer> requestBody) {

        int scheduleId = requestBody.get("scheduleId");
        BusCrew driver = busCrewService.findDriverByScheduleId(scheduleId);
        return ResponseEntity.ok().body(driver);
    }

    @RequestMapping(value = "/findConductorByScheduleId", method = RequestMethod.POST)
    public ResponseEntity<?> findConductorByScheduleId(@RequestBody Map<String, Integer> requestBody) {

        int scheduleId = requestBody.get("scheduleId");
        BusCrew conductor = busCrewService.findConductorByScheduleId(scheduleId);
        return ResponseEntity.ok().body(conductor);
    }

    @RequestMapping(value = "/findScheduleByCrewUserIdDate", method = RequestMethod.POST)
    public ResponseEntity<?> findScheduleByCrewUserIdDate(@RequestBody Map<String,String> requestBody){
        List<Schedule> scheduleList = new ArrayList<>();
        List<Map<String,Object>> resultList = new ArrayList<>();

        int userId = (requestBody.get("userId") != "" ? Integer.parseInt(requestBody.get("userId")) : 0) ;
        int userTypeId = (requestBody.get("userTypeId") != "" ? Integer.parseInt(requestBody.get("userTypeId")) : 0) ;
        String searchDate = requestBody.get("date") ;

        if(userTypeId == 1){
            //admin
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date = sdf.parse(searchDate);
                scheduleList = scheduleService.findScheduleByDate(date);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

        }else if(userTypeId == 2){
            //bus owner
            List<BusOwner> busOwnerList = busOwnerService.findBusOwnerByUserId(userId);
            if(!busOwnerList.isEmpty()){
                BusOwner busOwner = busOwnerList.get(0);
                scheduleList = scheduleService.findScheduleByBusOwnerIdDate(searchDate, busOwner.getBusOwnerId());
            }

        }else if(userTypeId == 4 || userTypeId == 5){
            //bus crew
            List<BusCrew> busCrewList = busCrewService.findBusCrewByUserId(userId);
            if(!busCrewList.isEmpty()){

                BusCrew busCrew = busCrewList.get(0);
                scheduleList = scheduleService.findBusCrewTodaySchedule(searchDate, busCrew.getBusCrewId());
            }

        }


        if(scheduleList != null && !scheduleList.isEmpty()){
            for(Schedule schedule : scheduleList){
                Map<String,Object> scheduleResult = new HashMap<>();

                int scheduleId = schedule.getScheduleId();
                BusCrew driver = busCrewService.findDriverByScheduleId(scheduleId);
                BusCrew conductor = busCrewService.findConductorByScheduleId(scheduleId);

                String availableSeatsStr = getBookedSeatAmountByScheduleId(schedule.getScheduleId());

                scheduleResult.put("schedule",schedule);
                scheduleResult.put("driverName",(driver != null ? driver.getName() : ""));
                scheduleResult.put("conductorName",(conductor != null ? conductor.getName() : ""));
                scheduleResult.put("availableSeats",availableSeatsStr);

                resultList.add(scheduleResult);

            }
        }


        return ResponseEntity.ok().body(resultList.toArray());
    }

}

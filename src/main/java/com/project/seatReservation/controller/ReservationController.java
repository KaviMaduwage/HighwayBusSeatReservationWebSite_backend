package com.project.seatReservation.controller;

import com.project.seatReservation.model.*;
import com.project.seatReservation.service.ReservationService;
import com.project.seatReservation.service.ScheduleService;
import com.project.seatReservation.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
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

        String message = "";

        int userId = Integer.parseInt(requestBody.get("userId"));
        int scheduleId = Integer.parseInt(requestBody.get("reserveScheduleId"));
        String pickUpPoint = requestBody.get("pickUpPoint");
        String dropOffPoint = requestBody.get("dropOffPoint");
        String remark = requestBody.get("remark");

        List<Schedule> scheduleList = scheduleService.findScheduleById(scheduleId);
        User user = userService.findUserByUserId(userId);

        if(scheduleList != null && !scheduleList.isEmpty()){

            List<BlockedSeat> blockedSeats = reservationService.findBlockedSeatsByScheduleIdAndUserId(scheduleList.get(0).getScheduleId(), userId);

            List<Cart> cartList = reservationService.findCartByScheduleIdAndUserId(scheduleList.get(0).getScheduleId(), userId);

            if(cartList.isEmpty()){
                if(blockedSeats != null && blockedSeats.size()>0 ){
                    String blockSeatsStr = "";
                    String blockSeatIdsStr = "";
                    int seatCount = 0;

                    for(BlockedSeat blockedSeat : blockedSeats){
                        blockSeatsStr += blockedSeat.getRow()+" - "+blockedSeat.getCol()+",";
                        blockSeatIdsStr += blockedSeat.getBlockedSeatId()+",";
                        seatCount++;
                    }

                    blockSeatsStr = blockSeatsStr.substring(0,blockSeatsStr.length()-1).trim();
                    blockSeatIdsStr = blockSeatIdsStr.substring(0,blockSeatIdsStr.length()-1).trim();


                    Cart cart = new Cart();
                    cart.setSchedule(scheduleList.get(0));
                    cart.setUser(user);
                    cart.setDropPoint(dropOffPoint);
                    cart.setPickUpPoint(pickUpPoint);
                    cart.setRemark(remark);
                    cart.setSeatNos(blockSeatsStr);
                    cart.setBlockedSeatIds(blockSeatIdsStr);
                    cart.setPrice(seatCount*scheduleList.get(0).getTicketPrice());



                    Cart savedCart = reservationService.saveCart(cart);
                    List<CartAddedBlockedSeat> cartAddedBlockedSeatList = new ArrayList<>();
                    for(BlockedSeat blockedSeat : blockedSeats){
                        CartAddedBlockedSeat cartAddedBlockedSeat = new CartAddedBlockedSeat();
                        cartAddedBlockedSeat.setCart(cart);
                        cartAddedBlockedSeat.setBlockedSeat(blockedSeat);

                        cartAddedBlockedSeatList.add(cartAddedBlockedSeat);
                    }

                    reservationService.saveCartAddedBlockedSeats(cartAddedBlockedSeatList);


                    message = "Successfully added to the cart. Proceed with other reservations.";


                }else{
                    message = "No selected seats to add the cart. Time limit has exceeded.";
                }

            }else{
                message = "You have already added to the cart. Please delete the cart if you want to change the seats.";
            }

        }else{
            message = "Schedule is currently unavailable.";
        }

        return ResponseEntity.ok().body(message);
    }
    @RequestMapping(value = "/loadCartListByUserId",method = RequestMethod.POST)
    public ResponseEntity<?> loadCartListByUserId(@RequestBody Map<String, Integer> requestBody){
        List<Cart> cartList = new ArrayList<>();
        int userId = requestBody.get("userId");

        cartList = reservationService.findCartDetailsByUserId(userId);

        return ResponseEntity.ok().body(cartList.toArray());
    }

    @RequestMapping(value = "/deleteCartByCartId", method = RequestMethod.POST)
    public ResponseEntity<?> deleteCartByCartId(@RequestBody Map<String, Integer> requestBody){
        String message = "";
        int cartId = requestBody.get("cartId");
        List<Integer> cartIdList = new ArrayList<>();
        cartIdList.add(cartId);

        reservationService.deleteCartList(cartIdList);

        List<Cart> cartList = reservationService.findCartById(cartId);
        if(cartList.isEmpty()){
            message = "Successfully deleted.";
        }else{
            message = "Can't delete.";
        }
        return ResponseEntity.ok().body(message);
    }


}

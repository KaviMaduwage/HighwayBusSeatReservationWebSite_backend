package com.project.seatReservation.schedule;

import com.project.seatReservation.model.BlockedSeat;
import com.project.seatReservation.model.Cart;
import com.project.seatReservation.model.CartAddedBlockedSeat;
import com.project.seatReservation.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

@EnableScheduling
@Component
public class ScheduleTasks {
    ReservationService reservationService;

    public ScheduleTasks(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Scheduled(fixedRate = 15 * 60 * 1000)
    public void deleteBlockedSeatRecords(){
        //delete only blocked seats
        List<BlockedSeat> blockedSeats = reservationService.findBlockedSeatRecordsWithinThirtyMin();
        List<Integer> idList = new ArrayList<>();
        List<Integer> cartAddedSeatIdList = new ArrayList<>();
        for(BlockedSeat blockedSeat : blockedSeats){
            idList.add(blockedSeat.getBlockedSeatId());
        }

        List<BlockedSeat> cartAddedBlockedSeatList = reservationService.findCartAddedBlockedSeats(idList);
        List<BlockedSeat> onlyBlockedSeatList = new ArrayList<>();

        for(BlockedSeat b : cartAddedBlockedSeatList){
            cartAddedSeatIdList.add(b.getBlockedSeatId());
        }

        for(BlockedSeat b : blockedSeats){
            if(!cartAddedSeatIdList.contains(b.getBlockedSeatId())){
                onlyBlockedSeatList.add(b);
            }
        }
        reservationService.deleteBlockedSeatList(onlyBlockedSeatList);

        // delete card added blocked seats when cart added time exceeds given time

        List<Cart> cartList = reservationService.findCartSeatRecordsWithinThirtyMin();
        List<Integer> cartIds = new ArrayList<>();
        for(Cart c : cartList){
            cartIds.add(c.getCartId());
        }

        reservationService.deleteCartList(cartIds);
    }

}

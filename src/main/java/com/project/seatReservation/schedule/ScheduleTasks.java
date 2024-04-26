package com.project.seatReservation.schedule;

import com.project.seatReservation.model.BlockedSeat;
import com.project.seatReservation.service.ReservationService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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
        List<BlockedSeat> blockedSeats = reservationService.findBlockedSeatRecordsWithinThirtyMin();
        reservationService.deleteBlockedSeatList(blockedSeats);
    }

}

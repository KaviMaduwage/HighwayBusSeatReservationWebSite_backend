package com.project.seatReservation.service;

import com.project.seatReservation.model.BlockedSeat;
import com.project.seatReservation.model.Reservation;

import java.util.List;

public interface ReservationService {
    List<Reservation> findReservationsByScheduleId(int scheduleId);

    BlockedSeat saveBlockedSeat(BlockedSeat blockedSeat);

    List<BlockedSeat> findBlockedSeatsByScheduleId(int scheduleId);

    BlockedSeat findBlockedSeatByScheduleIdUserIdRowAndCol(int scheduleId, int rowNo, int colNo, int userId);

    void deleteBlockedSeat(BlockedSeat savedBlockedSeat);

    List<BlockedSeat> findBlockedSeatRecordsWithinThirtyMin();

    void deleteBlockedSeatList(List<BlockedSeat> blockedSeats);
}

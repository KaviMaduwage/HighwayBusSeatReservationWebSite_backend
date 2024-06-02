package com.project.seatReservation.service;

import com.project.seatReservation.model.*;

import java.util.List;

public interface ReservationService {
    List<Reservation> findReservationsByScheduleId(int scheduleId);

    BlockedSeat saveBlockedSeat(BlockedSeat blockedSeat);

    List<BlockedSeat> findBlockedSeatsByScheduleId(int scheduleId);

    BlockedSeat findBlockedSeatByScheduleIdUserIdRowAndCol(int scheduleId, int rowNo, int colNo, int userId);

    void deleteBlockedSeat(BlockedSeat savedBlockedSeat);

    List<BlockedSeat> findBlockedSeatRecordsWithinThirtyMin();

    void deleteBlockedSeatList(List<BlockedSeat> blockedSeats);

    List<BlockedSeat> findBlockedSeatsByScheduleIdAndUserId(int scheduleId, int userId);

    Cart saveCart(Cart cart);

    List<Cart> findCartDetailsByUserId(int userId);

    List<BlockedSeat> findCartAddedBlockedSeats(List<Integer> idList);

    List<Cart> findCartSeatRecordsWithinThirtyMin();

    void deleteCartList(List<Integer> cartList);

    void deleteBlockedSeatsById(List<Integer> cartAddedBlockedSeatsIds);

    void saveCartAddedBlockedSeats(List<CartAddedBlockedSeat> cartAddedBlockedSeatList);

    List<Cart> findCartById(int cartId);

    List<Cart> findCartByScheduleIdAndUserId(int scheduleId, int userId);

    List<Reservation> saveReservations(List<Reservation> newReservations);

    Reservation saveReservation(Reservation reservation);

    void saveSeatReservations(List<SeatReservation> seatReservationList);

    List<Reservation> findReservationsByRevIdList(List<Integer> reservationIdList);

    void updateReservations(List<Reservation> toBeUpdateReservations);

    List<SeatReservation> findReservedSeatsByRevId(int reservationId);

    void updateSeatReservations(List<SeatReservation> toBeUpdateSeatReservations);

    List<BlockedSeat> findBlockedSeatsByUserId(int userId);

    List<CartAddedBlockedSeat> findCartAddedBlockedSeatByCartIdList(List<Integer> cartIdList);

    void deleteCartDataByUserId(int userId);

    List<SeatReservation> findReservedSeatsByScheduleId(int scheduleId);

    List<Reservation> findReservationsByUserId(int userId);

    List<SeatReservation> findSuccessReservedSeatsByRevId(int reservationId);

    Reservation findReservationByRevId(Integer reservationId);

    List<Seat> findReservedSeatsByReservationId(int reservationId);

    List<SeatReservation> findSeatReservationsBYId(List<Integer> cancelSeatList);

    void updateReservation(Reservation reservation);

    List<SeatReservation> getUpcomingReservationsByUserId(int userId);

    List<SeatReservation> getCancelledReservations(int userId);
}

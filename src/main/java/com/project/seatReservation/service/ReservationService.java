package com.project.seatReservation.service;

import com.project.seatReservation.model.BlockedSeat;
import com.project.seatReservation.model.Cart;
import com.project.seatReservation.model.CartAddedBlockedSeat;
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
}

package com.project.seatReservation.dao;

import com.project.seatReservation.model.CartAddedBlockedSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartAddedBlockedSeatDao extends JpaRepository<CartAddedBlockedSeat, Integer> {
    @Query("SELECT c FROM CartAddedBlockedSeat c WHERE c.blockedSeat.blockedSeatId IN ( :idList)")
    List<CartAddedBlockedSeat> findCartAddedBlockedSeats(List<Integer> idList);

    @Query("SELECT c FROM CartAddedBlockedSeat c WHERE c.cart.cartId IN ( :cartIdList)")
    List<CartAddedBlockedSeat> findCartAddedBlockedSeatsByCartIds(List<Integer> cartIdList);
}

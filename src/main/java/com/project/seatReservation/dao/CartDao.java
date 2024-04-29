package com.project.seatReservation.dao;

import com.project.seatReservation.model.BlockedSeat;
import com.project.seatReservation.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

public interface CartDao extends JpaRepository<Cart, Integer> {

    @Query("SELECT c FROM Cart c WHERE c.user.userId = :userId")
    List<Cart> findCartDetailsByUserId(int userId);

    @Query("SELECT c FROM Cart c WHERE (:idList) LIKE CONCAT('%', c.blockedSeatIds, '%')")
    List<BlockedSeat> findCartAddedBlockedSeats(List<Integer> idList);

    @Query("SELECT c FROM Cart  c WHERE c.modifiedTime <= :timeDuration")
    List<Cart> findBlockedSeatRecordsWithinThirtyMin(Timestamp timeDuration);

    @Query("SELECT c FROM Cart  c WHERE c.cartId = :cartId")
    List<Cart> findCartById(int cartId);

    @Query("SELECT c FROM Cart c WHERE c.schedule.scheduleId = :scheduleId AND c.user.userId = :userId")
    List<Cart> findCartByScheduleIdAndUserId(int scheduleId, int userId);
}

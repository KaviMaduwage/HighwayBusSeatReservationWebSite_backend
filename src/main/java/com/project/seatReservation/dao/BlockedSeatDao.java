package com.project.seatReservation.dao;

import com.project.seatReservation.model.BlockedSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

public interface BlockedSeatDao extends JpaRepository<BlockedSeat, Integer> {

    @Query("SELECT b FROM BlockedSeat  b WHERE b.schedule.scheduleId = :scheduleId")
    List<BlockedSeat> findBlockedSeatsByScheduleId(int scheduleId);

    @Query("SELECT b FROM BlockedSeat  b WHERE b.schedule.scheduleId = :scheduleId AND b.row = :rowNo AND b.col = :colNo AND b.user.userId = :userId")
    BlockedSeat findBlockedSeatByScheduleIdUserIdRowAndCol(int scheduleId, int rowNo, int colNo, int userId);

    @Query("SELECT b FROM BlockedSeat  b WHERE b.modifiedTime <= :timeDuration")
    List<BlockedSeat> findBlockedSeatRecordsWithinThirtyMin(Timestamp timeDuration);

    @Query("SELECT b FROM BlockedSeat  b WHERE b.schedule.scheduleId = :scheduleId AND b.user.userId = :userId")
    List<BlockedSeat> findBlockedSeatsByScheduleIdAndUserId(int scheduleId, int userId);

    @Query("SELECT b FROM BlockedSeat b INNER JOIN CartAddedBlockedSeat cb ON cb.blockedSeat.blockedSeatId = b.blockedSeatId WHERE b.blockedSeatId IN ( :idList)")
    List<BlockedSeat> findCartAddedBlockedSeats(List<Integer> idList);

    @Query("SELECT b FROM BlockedSeat b INNER JOIN CartAddedBlockedSeat cb ON cb.blockedSeat.blockedSeatId = b.blockedSeatId WHERE cb.cart.cartId IN ( :idList)")
    List<BlockedSeat> findCartAddedBlockedSeatsByCartIdList(List<Integer> idList);
}

package com.project.seatReservation.dao;

import com.project.seatReservation.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SeatDao extends JpaRepository<Seat,Integer> {
    @Query("SELECT s FROM Seat s WHERE s.bus.busId = :busId AND s.isLocked = false")
    List<Seat> findSeatStructureByBusId(int busId);

    @Query("SELECT s FROM Seat s WHERE CONCAT(s.rowNo,' - ',s.columnNo) IN ( :seatNos) AND s.bus.busId = :busId ")
    List<Seat> getSeatsBySeatNoStr(List<String> seatNos, int busId);

    @Query("SELECT s FROM Seat s INNER JOIN SeatReservation sr ON sr.seat.seatId = s.seatId WHERE sr.reservation.reservationId = :reservationId AND sr.status = 'Success'" )
    List<Seat> findReservedSeatsByReservationId(int reservationId);

    @Query("SELECT s FROM Seat s WHERE s.bus.busId = :busId AND s.rowNo = :row AND s.columnNo = :col")
    List<Seat> findSeatsByBusIdRowandColNo(int busId, int row, int col);
}

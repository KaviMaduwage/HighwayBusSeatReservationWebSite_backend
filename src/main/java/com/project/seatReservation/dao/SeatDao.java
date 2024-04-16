package com.project.seatReservation.dao;

import com.project.seatReservation.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SeatDao extends JpaRepository<Seat,Integer> {
    @Query("SELECT s FROM Seat s WHERE s.bus.busId = :busId")
    List<Seat> findSeatStructureByBusId(int busId);
}

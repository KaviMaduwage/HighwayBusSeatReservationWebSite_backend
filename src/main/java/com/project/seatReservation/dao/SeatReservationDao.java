package com.project.seatReservation.dao;

import com.project.seatReservation.model.SeatReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SeatReservationDao extends JpaRepository<SeatReservation,Integer> {

    @Query("SELECT sr FROM SeatReservation sr WHERE sr.reservation.reservationId = :reservationId")
    List<SeatReservation> findReservedSeatsByRevId(int reservationId);

    @Query("SELECT sr FROM SeatReservation sr INNER JOIN Reservation  r ON r.reservationId = sr.reservation.reservationId WHERE r.schedule.scheduleId = :scheduleId")
    List<SeatReservation> findReservedSeatsByScheduleId(int scheduleId);
}

package com.project.seatReservation.dao;

import com.project.seatReservation.model.SeatReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface SeatReservationDao extends JpaRepository<SeatReservation,Integer> {

    @Query("SELECT sr FROM SeatReservation sr WHERE sr.reservation.reservationId = :reservationId")
    List<SeatReservation> findReservedSeatsByRevId(int reservationId);

    @Query("SELECT sr FROM SeatReservation sr INNER JOIN Reservation  r ON r.reservationId = sr.reservation.reservationId WHERE r.schedule.scheduleId = :scheduleId AND sr.status='Success'")
    List<SeatReservation> findReservedSeatsByScheduleId(int scheduleId);

    @Query("SELECT sr FROM SeatReservation sr WHERE sr.reservation.reservationId = :reservationId AND sr.status = 'Success'")
    List<SeatReservation> findSuccessReservedSeatsByRevId(int reservationId);

    @Query("SELECT sr FROM SeatReservation sr WHERE sr.seatReservationId IN ( :cancelSeatList)")
    List<SeatReservation> findSeatReservationsBYId(List<Integer> cancelSeatList);

    @Query("SELECT sr FROM SeatReservation sr WHERE sr.status='Success' AND DATE(sr.reservation.schedule.tripDateStr) >= :today AND sr.reservation.passenger.user.userId = :userId")
    List<SeatReservation> getUpcomingReservationsByUserId(int userId, Date today);

    @Query("SELECT sr FROM SeatReservation sr WHERE sr.status='Cancelled' AND sr.reservation.passenger.user.userId = :userId")

    List<SeatReservation> getCancelledReservations(int userId);
}

package com.project.seatReservation.dao;

import com.project.seatReservation.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReservationDao extends JpaRepository<Reservation,Integer> {
    @Query("SELECT r FROM Reservation r WHERE r.schedule.scheduleId = :scheduleId")
    List<Reservation> findReservationsByScheduleId(int scheduleId);

    @Query("SELECT r FROM Reservation  r WHERE r.reservationId IN ( :reservationIdList)")
    List<Reservation> findReservationsByRevIdList(List<Integer> reservationIdList);

    @Query("SELECT r FROM Reservation r WHERE r.passenger.user.userId = :userId AND r.isPaymentCompleted = true AND r.isCancelled = false")
    List<Reservation> findReservationsByUserId(int userId);

    @Query("SELECT r FROM Reservation r WHERE r.reservationId = :reservationId")
    Reservation findReservationByRevId(Integer reservationId);
}

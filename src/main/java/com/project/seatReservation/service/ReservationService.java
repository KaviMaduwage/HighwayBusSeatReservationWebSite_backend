package com.project.seatReservation.service;

import com.project.seatReservation.model.Reservation;

import java.util.List;

public interface ReservationService {
    List<Reservation> findReservationsByScheduleId(int scheduleId);
}

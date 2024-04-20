package com.project.seatReservation.service;

import com.project.seatReservation.dao.ReservationDao;
import com.project.seatReservation.model.Reservation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService{

    ReservationDao reservationDao;

    public ReservationServiceImpl(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    @Override
    public List<Reservation> findReservationsByScheduleId(int scheduleId) {
        return reservationDao.findReservationsByScheduleId(scheduleId);
    }
}

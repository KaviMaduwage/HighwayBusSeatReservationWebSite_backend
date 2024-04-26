package com.project.seatReservation.service;

import com.project.seatReservation.dao.BlockedSeatDao;
import com.project.seatReservation.dao.ReservationDao;
import com.project.seatReservation.model.BlockedSeat;
import com.project.seatReservation.model.Reservation;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService{

    ReservationDao reservationDao;
    BlockedSeatDao blockedSeatDao;

    public ReservationServiceImpl(ReservationDao reservationDao, BlockedSeatDao blockedSeatDao) {
        this.reservationDao = reservationDao;
        this.blockedSeatDao = blockedSeatDao;
    }

    @Override
    public List<Reservation> findReservationsByScheduleId(int scheduleId) {
        return reservationDao.findReservationsByScheduleId(scheduleId);
    }

    @Override
    public BlockedSeat saveBlockedSeat(BlockedSeat blockedSeat) {
        return blockedSeatDao.save(blockedSeat);
    }

    @Override
    public List<BlockedSeat> findBlockedSeatsByScheduleId(int scheduleId) {
        return blockedSeatDao.findBlockedSeatsByScheduleId(scheduleId);
    }

    @Override
    public BlockedSeat findBlockedSeatByScheduleIdUserIdRowAndCol(int scheduleId, int rowNo, int colNo, int userId) {
        return blockedSeatDao.findBlockedSeatByScheduleIdUserIdRowAndCol(scheduleId,rowNo,colNo,userId);
    }

    @Override
    public void deleteBlockedSeat(BlockedSeat savedBlockedSeat) {
        blockedSeatDao.delete(savedBlockedSeat);
    }

    @Override
    public List<BlockedSeat> findBlockedSeatRecordsWithinThirtyMin() {
        Timestamp timeDuration = Timestamp.from(Instant.now().minus(30, ChronoUnit.MINUTES));
        return blockedSeatDao.findBlockedSeatRecordsWithinThirtyMin(timeDuration);
    }

    @Override
    public void deleteBlockedSeatList(List<BlockedSeat> blockedSeats) {
        blockedSeatDao.deleteAll(blockedSeats);
    }
}

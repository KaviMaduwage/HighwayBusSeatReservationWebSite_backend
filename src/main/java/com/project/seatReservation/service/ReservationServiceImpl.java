package com.project.seatReservation.service;

import com.project.seatReservation.dao.*;
import com.project.seatReservation.model.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService{

    ReservationDao reservationDao;
    BlockedSeatDao blockedSeatDao;
    CartDao cartDao;
    CartAddedBlockedSeatDao cartAddedBlockedSeatDao;
    SeatReservationDao seatReservationDao;

    SeatDao seatDao;

    ScheduleDao scheduleDao;
    NotifySeatCancellationDao notifySeatCancellationDao;

    public ReservationServiceImpl(ReservationDao reservationDao, BlockedSeatDao blockedSeatDao, CartDao cartDao,
                                  CartAddedBlockedSeatDao cartAddedBlockedSeatDao,SeatReservationDao seatReservationDao,
                                  SeatDao seatDao, ScheduleDao scheduleDao,NotifySeatCancellationDao notifySeatCancellationDao) {
        this.reservationDao = reservationDao;
        this.blockedSeatDao = blockedSeatDao;
        this.cartDao = cartDao;
        this.cartAddedBlockedSeatDao = cartAddedBlockedSeatDao;
        this.seatReservationDao = seatReservationDao;
        this.seatDao = seatDao;
        this.scheduleDao = scheduleDao;
        this.notifySeatCancellationDao = notifySeatCancellationDao;
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

    @Override
    public List<BlockedSeat> findBlockedSeatsByScheduleIdAndUserId(int scheduleId, int userId) {
        return blockedSeatDao.findBlockedSeatsByScheduleIdAndUserId(scheduleId,userId);
    }

    @Override
    public Cart saveCart(Cart cart) {
        return cartDao.save(cart);

    }

    @Override
    public List<Cart> findCartDetailsByUserId(int userId) {
        return cartDao.findCartDetailsByUserId(userId);
    }



    @Override
    public List<BlockedSeat> findCartAddedBlockedSeats(List<Integer> idList) {
        return blockedSeatDao.findCartAddedBlockedSeats(idList);
    }

    @Override
    public List<Cart> findCartSeatRecordsWithinThirtyMin() {
        Timestamp timeDuration = Timestamp.from(Instant.now().minus(30, ChronoUnit.MINUTES));
        return cartDao.findBlockedSeatRecordsWithinThirtyMin(timeDuration);
    }

    @Override
    @Transactional
    public void deleteCartList(List<Integer> cartIdList) {
        List<BlockedSeat> blockedSeats = blockedSeatDao.findCartAddedBlockedSeatsByCartIdList(cartIdList);
        blockedSeatDao.deleteAll(blockedSeats);

        List<CartAddedBlockedSeat> cartAddedBlockedSeatList = cartAddedBlockedSeatDao.findCartAddedBlockedSeatsByCartIds(cartIdList);

        cartDao.deleteAllById(cartIdList);

        cartAddedBlockedSeatDao.deleteAll(cartAddedBlockedSeatList);

        // all data is deleted at last due to transactional annotation
    }

    @Override
    public void deleteBlockedSeatsById(List<Integer> cartAddedBlockedSeatsIds) {
        blockedSeatDao.deleteAllById(cartAddedBlockedSeatsIds);
    }

    @Override
    public void saveCartAddedBlockedSeats(List<CartAddedBlockedSeat> cartAddedBlockedSeatList) {
        cartAddedBlockedSeatDao.saveAll(cartAddedBlockedSeatList);
    }

    @Override
    public List<Cart> findCartById(int cartId) {
        return cartDao.findCartById(cartId);
    }

    @Override
    public List<Cart> findCartByScheduleIdAndUserId(int scheduleId, int userId) {
        return cartDao.findCartByScheduleIdAndUserId(scheduleId,userId);
    }

    @Override
    public List<Reservation> saveReservations(List<Reservation> newReservations) {
        return reservationDao.saveAll(newReservations);
    }

    @Override
    public Reservation saveReservation(Reservation reservation) {
        return reservationDao.save(reservation);
    }

    @Override
    public void saveSeatReservations(List<SeatReservation> seatReservationList) {
        seatReservationDao.saveAll(seatReservationList);
    }

    @Override
    public List<Reservation> findReservationsByRevIdList(List<Integer> reservationIdList) {
        return reservationDao.findReservationsByRevIdList(reservationIdList);
    }

    @Override
    @Transactional
    public void updateReservations(List<Reservation> toBeUpdateReservations) {
        reservationDao.saveAll(toBeUpdateReservations);
    }

    @Override
    public List<SeatReservation> findReservedSeatsByRevId(int reservationId) {
        return seatReservationDao.findReservedSeatsByRevId(reservationId);
    }

    @Override
    @Transactional
    public void updateSeatReservations(List<SeatReservation> toBeUpdateSeatReservations) {
        seatReservationDao.saveAll(toBeUpdateSeatReservations);
    }

    @Override
    public List<BlockedSeat> findBlockedSeatsByUserId(int userId) {
        return blockedSeatDao.findBlockedSeatsByUserId(userId);
    }

    @Override
    public List<CartAddedBlockedSeat> findCartAddedBlockedSeatByCartIdList(List<Integer> cartIdList) {
        return cartAddedBlockedSeatDao.findCartAddedBlockedSeatsByCartIds(cartIdList);
    }

    @Override
    @Transactional
    public void deleteCartDataByUserId(int userId) {
        List<Cart> cartList = cartDao.findCartDetailsByUserId(userId);
        List<BlockedSeat> blockedSeatList = blockedSeatDao.findBlockedSeatsByUserId(userId);
        List<Integer> cartIdList = new ArrayList<>();

        for(Cart c : cartList){
            cartIdList.add(c.getCartId());
        }

        List<CartAddedBlockedSeat> cartAddedBlockedSeatList = cartAddedBlockedSeatDao.findCartAddedBlockedSeatsByCartIds(cartIdList);

        cartAddedBlockedSeatDao.deleteAll(cartAddedBlockedSeatList);
        cartDao.deleteAll(cartList);
        blockedSeatDao.deleteAll(blockedSeatList);

    }

    @Override
    public List<SeatReservation> findReservedSeatsByScheduleId(int scheduleId) {
        return seatReservationDao.findReservedSeatsByScheduleId(scheduleId);
    }

    @Override
    public List<Reservation> findReservationsByUserId(int userId) {
        return reservationDao.findReservationsByUserId(userId);
    }

    @Override
    public List<SeatReservation> findSuccessReservedSeatsByRevId(int reservationId) {
        return seatReservationDao.findSuccessReservedSeatsByRevId(reservationId);
    }

    @Override
    public Reservation findReservationByRevId(Integer reservationId) {
        return reservationDao.findReservationByRevId(reservationId);
    }

    @Override
    public List<Seat> findReservedSeatsByReservationId(int reservationId) {
        return seatDao.findReservedSeatsByReservationId(reservationId);
    }

    @Override
    public List<SeatReservation> findSeatReservationsBYId(List<Integer> cancelSeatList) {
        return seatReservationDao.findSeatReservationsBYId(cancelSeatList);
    }

    @Override
    @Transactional
    public void updateReservation(Reservation reservation) {
        reservationDao.save(reservation);
    }

    @Override
    public List<SeatReservation> getUpcomingReservationsByUserId(int userId) {
        return seatReservationDao.getUpcomingReservationsByUserId(userId, new Date());
    }

    @Override
    public List<SeatReservation> getCancelledReservations(int userId) {
        return seatReservationDao.getCancelledReservations(userId);
    }

    @Override
    public List<Schedule> getTodaysScheduleByUserId(int userId) {
        return scheduleDao.getTodaysScheduleByUserId(userId,new Date());
    }

    @Override
    public List<NotifySeatCancellation> findNotifySeatCancellationsByPassengerIdScheduleId(int passengerId, int scheduleId) {
        return notifySeatCancellationDao.findNotifySeatCancellationsByPassengerIdScheduleId(passengerId,scheduleId);
    }

    @Override
    public void saveNotifySeatCancellation(NotifySeatCancellation nsc) {
        notifySeatCancellationDao.save(nsc);
    }

    @Override
    public void deleteNotifySeatCancellation(NotifySeatCancellation notifySeatCancellation) {
        notifySeatCancellationDao.delete(notifySeatCancellation);
    }

    @Override
    public List<NotifySeatCancellation> findNotifySeatCancellationsByScheduleId(int scheduleId) {
        return notifySeatCancellationDao.findNotifySeatCancellationsByScheduleId(scheduleId);
    }
}


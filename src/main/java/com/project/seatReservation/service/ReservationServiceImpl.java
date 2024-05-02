package com.project.seatReservation.service;

import com.project.seatReservation.dao.BlockedSeatDao;
import com.project.seatReservation.dao.CartAddedBlockedSeatDao;
import com.project.seatReservation.dao.CartDao;
import com.project.seatReservation.dao.ReservationDao;
import com.project.seatReservation.model.BlockedSeat;
import com.project.seatReservation.model.Cart;
import com.project.seatReservation.model.CartAddedBlockedSeat;
import com.project.seatReservation.model.Reservation;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService{

    ReservationDao reservationDao;
    BlockedSeatDao blockedSeatDao;
    CartDao cartDao;
    CartAddedBlockedSeatDao cartAddedBlockedSeatDao;

    public ReservationServiceImpl(ReservationDao reservationDao, BlockedSeatDao blockedSeatDao, CartDao cartDao,
                                  CartAddedBlockedSeatDao cartAddedBlockedSeatDao) {
        this.reservationDao = reservationDao;
        this.blockedSeatDao = blockedSeatDao;
        this.cartDao = cartDao;
        this.cartAddedBlockedSeatDao = cartAddedBlockedSeatDao;
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
}

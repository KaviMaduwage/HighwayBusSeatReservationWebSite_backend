package com.project.seatReservation.service;

import com.project.seatReservation.dao.BusDao;
import com.project.seatReservation.dao.SeatDao;
import com.project.seatReservation.model.Bus;
import com.project.seatReservation.model.Seat;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

@Service
public class BusServiceImpl implements BusService{
    BusDao busDao;

    SeatDao seatDao;

    public BusServiceImpl(BusDao busDao, SeatDao seatDao) {
        this.busDao = busDao;
        this.seatDao = seatDao;
    }

    @Override
    public List<Bus> findBusByPlateNo(String plateNo) {
        return busDao.findBusByPlateNo(plateNo);
    }

    @Override
    public void saveBusDetails(Bus bus) {
        busDao.save(bus);
    }

    @Override
    @Transactional
    public void updateBusDetail(Bus bus) {
        busDao.save(bus);
    }

    @Override
    public List<Bus> loadAllBusDetails() {
        return busDao.findAll();
    }

    @Override
    public Bus findBusById(int busId) {
        return busDao.findBusById(busId);
    }

    @Override
    public void saveSeatStructure(List<Seat> seatList) {
        seatDao.saveAll(seatList);
    }

    @Override
    public List<Seat> findSeatStructureByBusId(int busId) {
        return seatDao.findSeatStructureByBusId(busId);
    }

    @Override
    public List<Bus> loadAllBusDetailsInTravelService(int busOwnerId) {
        return busDao.loadAllBusDetailsInTravelService(busOwnerId);
    }

    @Override
    public List<Seat> getSeatsBySeatNoStr(String[] seatNos, int busId) {
        List<String> seatNosList = Arrays.asList(seatNos);
        return seatDao.getSeatsBySeatNoStr(seatNosList,busId);
    }
}

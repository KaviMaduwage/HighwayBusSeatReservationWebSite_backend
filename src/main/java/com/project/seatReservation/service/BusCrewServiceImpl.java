package com.project.seatReservation.service;

import com.project.seatReservation.dao.BusCrewDao;
import com.project.seatReservation.dao.BusCrewTypeDao;
import com.project.seatReservation.model.BusCrew;
import com.project.seatReservation.model.BusCrewType;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class BusCrewServiceImpl implements BusCrewService{

    BusCrewTypeDao busCrewTypeDao;
    BusCrewDao busCrewDao;

    public BusCrewServiceImpl(BusCrewTypeDao busCrewTypeDao, BusCrewDao busCrewDao) {
        this.busCrewTypeDao = busCrewTypeDao;
        this.busCrewDao = busCrewDao;
    }

    @Override
    public List<BusCrewType> getBusCrewTypes() {
        return busCrewTypeDao.findAll();
    }

    @Override
    public BusCrew saveBusCrew(BusCrew busCrew) {
        return busCrewDao.save(busCrew);
    }

    @Override
    public List<BusCrew> getBusCrewByOwnerId(int busOwnerId) {
        return busCrewDao.getBusCrewByOwnerId(busOwnerId);
    }

    @Override
    public BusCrew findBusCrewById(int memberId) {
        return busCrewDao.findBusCrewById(memberId);
    }

    @Override
    @Transactional
    public void updateBusCrew(BusCrew busCrew) {

        busCrewDao.save(busCrew);
    }

    @Override
    public void deleteStaffMember(BusCrew busCrew) {
        busCrewDao.delete(busCrew);
    }
}

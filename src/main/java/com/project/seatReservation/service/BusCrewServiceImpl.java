package com.project.seatReservation.service;

import com.project.seatReservation.dao.BusCrewDao;
import com.project.seatReservation.dao.BusCrewTypeDao;
import com.project.seatReservation.dao.TripCrewDao;
import com.project.seatReservation.model.BusCrew;
import com.project.seatReservation.model.BusCrewType;
import com.project.seatReservation.model.TripCrew;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class BusCrewServiceImpl implements BusCrewService{

    BusCrewTypeDao busCrewTypeDao;
    BusCrewDao busCrewDao;

    TripCrewDao tripCrewDao;

    public BusCrewServiceImpl(BusCrewTypeDao busCrewTypeDao, BusCrewDao busCrewDao,TripCrewDao tripCrewDao) {
        this.busCrewTypeDao = busCrewTypeDao;
        this.busCrewDao = busCrewDao;
        this.tripCrewDao = tripCrewDao;
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

    @Override
    public List<BusCrew> findBusCrewByNameJobTypeStatusAndBusOwnerId(String namePhrase, int crewTypeId, String searchStatus, int busOwnerId) {
        return busCrewDao.findBusCrewByNameJobTypeStatusAndBusOwnerId(namePhrase,crewTypeId,searchStatus,busOwnerId);
    }

    @Override
    public List<BusCrew> loadBusCrewByTypeInTravelService(int busOwnerId, int crewTypeId) {
        return busCrewDao.loadBusCrewByTypeInTravelService(busOwnerId,crewTypeId);
    }

    @Override
    public List<BusCrew> findBusCrewByUserId(int userId) {
        return busCrewDao.findBusCrewByUserId(userId);
    }

    @Override
    public BusCrew findDriverByScheduleId(int scheduleId) {
        return busCrewDao.findDriverByScheduleId(scheduleId);
    }

    @Override
    public BusCrew findConductorByScheduleId(int scheduleId) {
        return busCrewDao.findConductorByScheduleId(scheduleId);
    }

    @Override
    public List<BusCrew> getDriverListByBusOwnerId(int busOwnerId) {
        return busCrewDao.getDriverListByBusOwnerId(busOwnerId);
    }

    @Override
    public List<BusCrew> getConductorListByBusOwnerId(int busOwnerId) {
        return busCrewDao.getConductorListByBusOwnerId(busOwnerId);
    }

    @Override
    public List<TripCrew> findTripsByCrewId(int busCrewId) {
        return tripCrewDao.findTripsByCrewId(busCrewId);
    }
}

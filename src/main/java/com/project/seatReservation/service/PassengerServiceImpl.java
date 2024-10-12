package com.project.seatReservation.service;

import com.project.seatReservation.dao.PassengerDao;
import com.project.seatReservation.dao.TempPassengerDao;
import com.project.seatReservation.model.Passenger;
import com.project.seatReservation.model.TempPassenger;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class PassengerServiceImpl implements PassengerService{
    PassengerDao passengerDao;
    TempPassengerDao tempPassengerDao;

    public PassengerServiceImpl(PassengerDao passengerDao,TempPassengerDao tempPassengerDao) {
        this.passengerDao = passengerDao;
        this.tempPassengerDao = tempPassengerDao;
    }

    @Override
    public List<Passenger> findPassengerByUserId(int userId) {
        return passengerDao.findPassengerByUserId(userId);
    }

    @Override
    public void savePassenger(Passenger passenger) {
        passengerDao.save(passenger);
    }

    @Override
    public void saveTempPassenger(TempPassenger tempPassenger) {
        tempPassengerDao.save(tempPassenger);
    }

    @Override
    public TempPassenger findTempPassengerByUserId(int userId) {
        return tempPassengerDao.findTempPassengerByUserId(userId);
    }

    @Override
    @Transactional
    public void updatePassenger(Passenger passenger) {
        passengerDao.save(passenger);
    }

    @Override
    public List<Passenger> findAllPassengers() {
        return passengerDao.findAll();
    }


}

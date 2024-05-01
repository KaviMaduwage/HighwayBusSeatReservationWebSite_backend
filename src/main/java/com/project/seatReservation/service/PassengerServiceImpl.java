package com.project.seatReservation.service;

import com.project.seatReservation.dao.PassengerDao;
import com.project.seatReservation.model.Passenger;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PassengerServiceImpl implements PassengerService{
    PassengerDao passengerDao;

    public PassengerServiceImpl(PassengerDao passengerDao) {
        this.passengerDao = passengerDao;
    }

    @Override
    public List<Passenger> findPassengerByUserId(int userId) {
        return passengerDao.findPassengerByUserId(userId);
    }

    @Override
    public void savePassenger(Passenger passenger) {
        passengerDao.save(passenger);
    }
}

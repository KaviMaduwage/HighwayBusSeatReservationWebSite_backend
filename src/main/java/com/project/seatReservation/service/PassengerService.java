package com.project.seatReservation.service;

import com.project.seatReservation.model.Passenger;
import com.project.seatReservation.model.TempPassenger;

import java.util.List;

public interface PassengerService {
    List<Passenger> findPassengerByUserId(int userId);

    void savePassenger(Passenger passenger);

    void saveTempPassenger(TempPassenger tempPassenger);

    TempPassenger findTempPassengerByUserId(int userId);

    void updatePassenger(Passenger passenger);
}

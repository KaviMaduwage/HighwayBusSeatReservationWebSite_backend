package com.project.seatReservation.service;

import com.project.seatReservation.model.Passenger;

import java.util.List;

public interface PassengerService {
    List<Passenger> findPassengerByUserId(int userId);

    void savePassenger(Passenger passenger);
}

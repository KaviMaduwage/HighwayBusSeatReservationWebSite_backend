package com.project.seatReservation.dao;

import com.project.seatReservation.model.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassengerDao extends JpaRepository<Passenger, Integer> {
}

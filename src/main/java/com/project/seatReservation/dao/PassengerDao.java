package com.project.seatReservation.dao;

import com.project.seatReservation.model.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PassengerDao extends JpaRepository<Passenger, Integer> {

    @Query("SELECT p FROM Passenger p WHERE p.user.userId = :userId")
    List<Passenger> findPassengerByUserId(int userId);
}

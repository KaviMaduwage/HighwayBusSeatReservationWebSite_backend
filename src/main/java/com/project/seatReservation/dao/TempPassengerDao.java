package com.project.seatReservation.dao;

import com.project.seatReservation.model.TempPassenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TempPassengerDao extends JpaRepository<TempPassenger,Integer> {

    @Query("SELECT tp FROM TempPassenger tp WHERE tp.user.userId = :userId")
    TempPassenger findTempPassengerByUserId(int userId);
}

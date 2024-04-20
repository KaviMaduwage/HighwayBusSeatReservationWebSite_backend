package com.project.seatReservation.dao;

import com.project.seatReservation.model.Bus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BusDao extends JpaRepository<Bus, Integer> {
    @Query("SELECT b FROM Bus b WHERE b.plateNo = :plateNo")
    List<Bus> findBusByPlateNo(String plateNo);

    @Query("SELECT b FROM Bus b WHERE b.busId = :busId")
    Bus findBusById(int busId);

    @Query("SELECT b FROM Bus b WHERE b.busOwner.busOwnerId = :busOwnerId")
    List<Bus> loadAllBusDetailsInTravelService(int busOwnerId);
}

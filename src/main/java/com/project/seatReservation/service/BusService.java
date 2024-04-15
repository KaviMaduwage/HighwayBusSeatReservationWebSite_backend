package com.project.seatReservation.service;

import com.project.seatReservation.model.Bus;
import com.project.seatReservation.model.Seat;

import java.util.List;

public interface BusService {
    List<Bus> findBusByPlateNo(String plateNo);

    void saveBusDetails(Bus bus);

    void updateBusDetail(Bus bus);

    List<Bus> loadAllBusDetails();

    Bus findBusById(int busId);

    void saveSeatStructure(List<Seat> seatList);

    List<Seat> findSeatStructureByBusId(int busId);
}

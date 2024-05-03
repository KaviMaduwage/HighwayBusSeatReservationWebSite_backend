package com.project.seatReservation.service;

import com.project.seatReservation.model.BusOwner;

import java.util.List;

public interface BusOwnerService {
    void saveBusOwner(BusOwner busOwner);

    List<BusOwner> findBusOwnerByUserId(int busOwnerUserId);

    List<BusOwner> findActiveBusOwnerList();
}

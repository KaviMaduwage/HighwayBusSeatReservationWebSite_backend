package com.project.seatReservation.service;

import com.project.seatReservation.model.BusCrew;
import com.project.seatReservation.model.BusCrewType;

import java.util.List;

public interface BusCrewService {
    List<BusCrewType> getBusCrewTypes();

    BusCrew saveBusCrew(BusCrew busCrew);

    List<BusCrew> getBusCrewByOwnerId(int busOwnerId);

    BusCrew findBusCrewById(int memberId);

    void updateBusCrew(BusCrew busCrew);

    void deleteStaffMember(BusCrew busCrew);

    List<BusCrew> findBusCrewByNameJobTypeStatusAndBusOwnerId(String namePhrase, int crewTypeId, String searchStatus, int busOwnerId);
}

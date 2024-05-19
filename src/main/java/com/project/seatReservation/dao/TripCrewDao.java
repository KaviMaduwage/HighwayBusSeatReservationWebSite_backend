package com.project.seatReservation.dao;

import com.project.seatReservation.model.TripCrew;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TripCrewDao extends JpaRepository<TripCrew, Integer> {

    @Query("SELECT tc FROM TripCrew tc WHERE tc.schedule.scheduleId = :scheduleId")
    List<TripCrew> getCrewListByScheduleId(int scheduleId);

    @Query("SELECT tc FROM TripCrew tc WHERE tc.busCrew.busCrewId = :busCrewId")
    List<TripCrew> findTripsByCrewId(int busCrewId);
}

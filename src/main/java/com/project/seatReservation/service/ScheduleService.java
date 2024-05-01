package com.project.seatReservation.service;

import com.project.seatReservation.model.BusCrew;
import com.project.seatReservation.model.Schedule;
import com.project.seatReservation.model.TripCrew;

import java.util.Date;
import java.util.List;

public interface ScheduleService {
    List<Schedule> findScheduleByOriginDestinationAndStartDateTime(Date tripDate, String origin, String destination, String startTime);

    Schedule saveSchedule(Schedule schedule);

    List<TripCrew> getCrewListByScheduleId(int scheduleId);

    void saveTripCrew(List<TripCrew> toBeSavedCrewList);

    Schedule updateSchedule(Schedule schedule);

    void updateTripCrew(TripCrew tripCrew);

    List<Schedule> findScheduleByDate(Date date);

    List<Schedule> findScheduleById(int scheduleId);

    List<Schedule> findBusScheduleByDateTownAndRoute(Date date, String origin, String destination, int routeId);

    List<Schedule> findBusCrewTodaySchedule(String today, int busCrewId);

    List<Schedule> findScheduleByBusOwnerIdDate(String searchDate, int busOwnerId);
}

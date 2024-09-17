package com.project.seatReservation.dao;

import com.project.seatReservation.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface ScheduleDao extends JpaRepository<Schedule, Integer> {

    @Query("SELECT s FROM Schedule s WHERE DATE(s.tripDateStr) = :tripDate AND s.origin = :origin AND s.destination = :destination AND s.tripStartTime = :startTime")
    List<Schedule> findScheduleByOriginDestinationAndStartDateTime(Date tripDate, String origin, String destination, String startTime);

    @Query("SELECT s FROM Schedule  s WHERE DATE(s.tripDateStr) = :date ")
    List<Schedule> findScheduleByDate(Date date);
    @Query("SELECT s FROM Schedule  s WHERE s.scheduleId = :scheduleId ")
    List<Schedule> findScheduleById(int scheduleId);

    @Query("SELECT s FROM Schedule s WHERE DATE(s.tripDateStr) = :date AND ('' = :origin OR '0' = :origin OR s.bus.route.routeDescription LIKE CONCAT('%',:origin,'%')) AND ('' = :destination OR '0' = :destination OR s.bus.route.routeDescription LIKE CONCAT('%',:destination,'%')) " +
            "AND (0 = :routeId OR s.bus.route.routeId = :routeId)")
    List<Schedule> findBusScheduleByDateTownAndRoute(Date date, String origin, String destination, int routeId);

    @Query("SELECT s FROM Schedule  s INNER JOIN TripCrew  tc ON tc.schedule.scheduleId = s.scheduleId WHERE tc.busCrew.busCrewId = :busCrewId AND s.tripDateStr = :today")
    List<Schedule> findBusCrewTodaySchedule(String today, int busCrewId);

    @Query("SELECT DISTINCT s FROM Schedule  s INNER JOIN TripCrew  tc ON tc.schedule.scheduleId = s.scheduleId WHERE tc.schedule.bus.busOwner.busOwnerId = :busOwnerId AND s.tripDateStr = :searchDate")
    List<Schedule> findScheduleByBusOwnerIdDate(String searchDate, int busOwnerId);

    @Query("SELECT s FROM Schedule s INNER JOIN Reservation  r ON r.schedule.scheduleId = s.scheduleId WHERE DATE(s.tripDateStr) = :date AND r.passenger.user.userId = :userId AND r.isCancelled = false AND r.isPaymentCompleted = true")
    List<Schedule> getTodaysScheduleByUserId(int userId, Date date);

    @Query("SELECT s FROM Schedule  s WHERE DATE(s.tripDateStr) > :today ")
    List<Schedule> findFutureSchedulesByDate(Date today);
}

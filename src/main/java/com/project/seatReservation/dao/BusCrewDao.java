package com.project.seatReservation.dao;

import com.project.seatReservation.model.BusCrew;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BusCrewDao extends JpaRepository<BusCrew, Integer> {
    @Query("SELECT b FROM BusCrew b WHERE b.busOwner.busOwnerId = :busOwnerId")
    List<BusCrew> getBusCrewByOwnerId(int busOwnerId);

    @Query("SELECT b FROM BusCrew b WHERE b.busCrewId = :memberId")
    BusCrew findBusCrewById(int memberId);

    @Query("SELECT b FROM BusCrew b WHERE ('' = :namePhrase OR b.name LIKE CONCAT('%', :namePhrase, '%')) AND ( 0 = :crewTypeId OR b.busCrewType.busCrewTypeId = :crewTypeId)" +
            "AND ('' = :searchStatus OR b.status = :searchStatus ) AND b.busOwner.busOwnerId = :busOwnerId")
    List<BusCrew> findBusCrewByNameJobTypeStatusAndBusOwnerId(String namePhrase, int crewTypeId, String searchStatus, int busOwnerId);

    @Query("SELECT b FROM BusCrew b WHERE b.busOwner.busOwnerId = :busOwnerId AND b.busCrewType.busCrewTypeId = :crewTypeId AND b.status LIKE 'present'")
    List<BusCrew> loadBusCrewByTypeInTravelService(int busOwnerId, int crewTypeId);

    @Query("SELECT b FROM BusCrew b WHERE b.user.userId = :userId")
    List<BusCrew> findBusCrewByUserId(int userId);

    @Query("SELECT b FROM BusCrew b INNER JOIN TripCrew tc ON tc.busCrew.busCrewId = b.busCrewId WHERE tc.schedule.scheduleId = :scheduleId AND b.busCrewType.busCrewTypeId = 1")
    BusCrew findDriverByScheduleId(int scheduleId);

    @Query("SELECT b FROM BusCrew b INNER JOIN TripCrew tc ON tc.busCrew.busCrewId = b.busCrewId WHERE tc.schedule.scheduleId = :scheduleId AND b.busCrewType.busCrewTypeId = 2")
    BusCrew findConductorByScheduleId(int scheduleId);

    @Query("SELECT b FROM BusCrew b WHERE b.busOwner.busOwnerId = :busOwnerId AND b.busCrewType.busCrewTypeId = 1 AND b.status = 'present'")
    List<BusCrew> getDriverListByBusOwnerId(int busOwnerId);

    @Query("SELECT b FROM BusCrew b WHERE b.busOwner.busOwnerId = :busOwnerId AND b.busCrewType.busCrewTypeId = 2 AND b.status = 'present'")
    List<BusCrew> getConductorListByBusOwnerId(int busOwnerId);
}

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
}

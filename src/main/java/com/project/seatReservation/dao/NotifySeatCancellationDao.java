package com.project.seatReservation.dao;

import com.project.seatReservation.model.NotifySeatCancellation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotifySeatCancellationDao extends JpaRepository<NotifySeatCancellation,Integer> {

    @Query("SELECT n FROM NotifySeatCancellation n WHERE n.schedule.scheduleId = :scheduleId AND n.passenger.passengerId = :passengerId")
    List<NotifySeatCancellation> findNotifySeatCancellationsByPassengerIdScheduleId(int passengerId, int scheduleId);

    @Query("SELECT n FROM NotifySeatCancellation n WHERE n.schedule.scheduleId = :scheduleId")
    List<NotifySeatCancellation> findNotifySeatCancellationsByScheduleId(int scheduleId);
}

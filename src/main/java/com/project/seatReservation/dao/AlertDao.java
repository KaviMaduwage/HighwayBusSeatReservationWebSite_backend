package com.project.seatReservation.dao;

import com.project.seatReservation.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface AlertDao extends JpaRepository<Alert,Integer> {

    @Query("SELECT a FROM Alert a WHERE DATE(a.createdDate) >= :pastDate AND a.forAdmin = true ORDER BY a.alertId DESC")
    List<Alert> getAllPrevious10DaysAlerts(Date pastDate);

    @Query("SELECT a FROM Alert a WHERE DATE(a.createdDate) >= :pastDate AND ((a.forTravelService = true AND a.schedule.bus.busOwner.user.userId = :userId) OR a.forAll = true)  ORDER BY a.alertId DESC")
    List<Alert> getAllAlertsForBusOwnerForPrevious10Days(Date pastDate, int userId);

    @Query("SELECT a FROM Alert a WHERE DATE(a.createdDate) >= :pastDate AND a.forAll = true  ORDER BY a.alertId DESC")
    List<Alert> getAllAlertsForOtherUsersForPrevious10Days(Date pastDate);

    @Query("SELECT a FROM Alert a WHERE a.createdBy.userId = :userId ORDER BY a.alertId DESC")
    List<Alert> getAlertsByUserId(int userId);

    @Query("SELECT a FROM Alert a WHERE a.alertId = :alertId")
    Alert findAlertById(int alertId);
}

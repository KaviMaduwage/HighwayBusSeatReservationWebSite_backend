package com.project.seatReservation.dao;

import com.project.seatReservation.model.AlertType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertTypeDao extends JpaRepository<AlertType,Integer> {
}

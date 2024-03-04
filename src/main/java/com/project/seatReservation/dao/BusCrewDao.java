package com.project.seatReservation.dao;

import com.project.seatReservation.model.BusCrew;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusCrewDao extends JpaRepository<BusCrew, Integer> {
}

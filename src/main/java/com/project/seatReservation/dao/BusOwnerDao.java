package com.project.seatReservation.dao;

import com.project.seatReservation.model.BusOwner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusOwnerDao extends JpaRepository<BusOwner, Integer> {
}

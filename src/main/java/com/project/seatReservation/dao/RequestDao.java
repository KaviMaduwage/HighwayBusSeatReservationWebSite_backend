package com.project.seatReservation.dao;

import com.project.seatReservation.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestDao extends JpaRepository<Request, Integer> {
}

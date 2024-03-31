package com.project.seatReservation.dao;

import com.project.seatReservation.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface RequestDao extends JpaRepository<Request, Integer> {
    @Query("SELECT r FROM Request r WHERE r.requestId = :requestId")
    Request findRequestById(int requestId);

}

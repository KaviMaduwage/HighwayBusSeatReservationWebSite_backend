package com.project.seatReservation.dao;

import com.project.seatReservation.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RouteDao extends JpaRepository<Route, Integer> {
    @Query("SELECT r FROM Route r WHERE r.routeNo = :routeNo")
    List<Route> findRouteByRouteNo(String routeNo);

    @Query("SELECT r FROM Route r WHERE r.routeId = :routeId")
    Route findRouteById(int routeId);
}

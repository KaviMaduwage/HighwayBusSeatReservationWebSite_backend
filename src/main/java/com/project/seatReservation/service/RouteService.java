package com.project.seatReservation.service;

import com.project.seatReservation.model.Route;

import java.util.List;

public interface RouteService {
    List<Route> findRouteByRouteNo(String routeNo);

    void saveRoute(Route route);

    List<Route> getAllRoutes();

    Route findRouteById(int routeId);

    void updateRoute(Route route);
}

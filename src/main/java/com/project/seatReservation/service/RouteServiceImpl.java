package com.project.seatReservation.service;

import com.project.seatReservation.dao.RouteDao;
import com.project.seatReservation.model.Route;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class RouteServiceImpl implements RouteService {
    RouteDao routeDao;

    public RouteServiceImpl(RouteDao routeDao) {
        this.routeDao = routeDao;
    }

    @Override
    public List<Route> findRouteByRouteNo(String routeNo) {
        return routeDao.findRouteByRouteNo(routeNo);
    }

    @Override
    public void saveRoute(Route route) {
        routeDao.save(route);
    }

    @Override
    public List<Route> getAllRoutes() {
        return routeDao.findAll();
    }

    @Override
    public Route findRouteById(int routeId) {
        return routeDao.findRouteById(routeId);
    }

    @Override
    @Transactional
    public void updateRoute(Route route) {
        routeDao.save(route);

    }
}

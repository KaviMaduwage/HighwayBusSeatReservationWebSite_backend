package com.project.seatReservation.controller;

import com.project.seatReservation.model.Route;
import com.project.seatReservation.service.RouteService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Controller
@CrossOrigin
public class RouteController {

    RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @RequestMapping(value = "/saveRoute", method = RequestMethod.POST)
    public ResponseEntity<String> saveRoute(@RequestBody Route route){
        String message = "";

        List<Route> existingRoute = routeService.findRouteByRouteNo(route.getRouteNo());

        if((existingRoute == null || existingRoute.isEmpty()) && route.getRouteId() == 0){
            // add
            routeService.saveRoute(route);
            message = "Successfully saved the route.";

        }else if(route.getRouteId() != 0){
            // update
            routeService.updateRoute(route);
            message = "Successfully updated. ";

        }else{
            message = "Route No exists";
        }


        return ResponseEntity.ok().body(message);
    }

    @RequestMapping(value = "/getAllRoutes", method = RequestMethod.POST)
    public ResponseEntity<?> getAllRoutes(){

        List<Route> routeList = new ArrayList<>();
        routeList = routeService.getAllRoutes();

        return ResponseEntity.ok().body(routeList.toArray());
    }

    @RequestMapping(value = "/findRouteById", method = RequestMethod.POST)
    public ResponseEntity<?> findRouteById(@RequestBody Map<String, Integer> requestBody){

        int routeId = requestBody.get("routeId");
        Route route = routeService.findRouteById(routeId);

        return ResponseEntity.ok().body(route);
    }
}

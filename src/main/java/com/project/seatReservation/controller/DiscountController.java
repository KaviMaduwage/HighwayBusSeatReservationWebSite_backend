package com.project.seatReservation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.seatReservation.model.BusOwner;
import com.project.seatReservation.model.Discount;
import com.project.seatReservation.model.Route;
import com.project.seatReservation.service.BusOwnerService;
import com.project.seatReservation.service.DiscountService;
import com.project.seatReservation.service.RouteService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@CrossOrigin
public class DiscountController {

    DiscountService discountService;

    RouteService routeService;

    BusOwnerService busOwnerService;

    public DiscountController(DiscountService discountService, RouteService routeService,BusOwnerService busOwnerService) {
        this.discountService = discountService;
        this.routeService = routeService;
        this.busOwnerService = busOwnerService;
    }

    @RequestMapping(value = "/saveDiscount",method = RequestMethod.POST)
    public ResponseEntity<?> saveDiscount(@RequestBody Map<String,Object> requestBody) throws ParseException {

        String message ="";

        Map<String,String> discountMap = (Map<String, String>) requestBody.get("discount");

        int userId = (int) requestBody.get("userId");

        Discount discount = new Discount();
        discount.setDiscountName(discountMap.get("discountName"));
        discount.setPercentage(Double.parseDouble(discountMap.get("percentage")));

        String startDateStr = discountMap.get("discountStartDate");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = simpleDateFormat.parse(startDateStr);
        discount.setDiscountStartDate(startDate);

        String endDateStr = discountMap.get("discountExpiryDate");
        Date endDate = simpleDateFormat.parse(endDateStr);
        discount.setDiscountExpiryDate(endDate);

        discount.setDescription(discountMap.get("description"));

        ObjectMapper objectMapper = new ObjectMapper();
        Route route = objectMapper.convertValue(discountMap.get("route"), Route.class);
        int routeId = route.getRouteId();
        if(routeId != 0){
            Route savedRoute = routeService.findRouteById(routeId);
            discount.setRoute(savedRoute);
        }

        List<BusOwner> busOwnerList = busOwnerService.findBusOwnerByUserId(userId);
        if(busOwnerList.size() > 0){
            discount.setBusOwner(busOwnerList.get(0));
        }

        discountService.saveDiscount(discount);
        message = "Successfully saved the discount";

        // check for earlier created schedules and apply the discounts for reservations

        return ResponseEntity.ok().body(message);

    }
    @RequestMapping(value = "/getAllDiscounts",method = RequestMethod.POST)
    public ResponseEntity<?> getAllDiscounts(){
        List<Discount> discounts = new ArrayList<>();
        discounts = discountService.getAllDiscounts();

        return ResponseEntity.ok().body(discounts.toArray());

    }
    @RequestMapping(value = "/getDiscountById", method = RequestMethod.POST)
    public ResponseEntity<?> getDiscountById(@RequestBody Map<String,Integer> requestBody){
        int discountId = requestBody.get("discountId");
        Discount discount = discountService.getDiscountById(discountId);

        return ResponseEntity.ok().body(discount);
    }
    @RequestMapping(value = "/searchDiscountsByDateRouteAndBusOwner", method = RequestMethod.POST)
    public ResponseEntity<?> searchDiscountsByDateRouteAndBusOwner(@RequestBody Map<String,String> requestBody){

        List<Discount> discounts = new ArrayList<>();
        String dateStr = (requestBody.get("fromDate") != null ? requestBody.get("fromDate") : "");
        String routeIdStr  = requestBody.get("routeId");
        int routeId = (routeIdStr.equals("") ? 0 : Integer.parseInt(routeIdStr));
        String busOwnerIdStr = requestBody.get("busOwnerId");
        int busOwnerId = (busOwnerIdStr.equals("") ? 0 : Integer.parseInt(busOwnerIdStr));

        discounts = discountService.findDiscountsByDateRouteAndBusOwner(dateStr,routeId,busOwnerId);

        return ResponseEntity.ok().body(discounts.toArray());

    }
}

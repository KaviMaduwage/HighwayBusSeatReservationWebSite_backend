package com.project.seatReservation.controller.reports;

import com.project.seatReservation.service.PassengerService;
import com.project.seatReservation.service.ReportService;
import com.project.seatReservation.service.ReservationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
@CrossOrigin
public class testReportController {
    ReservationService reservationService;
    ReportService reportService;
    PassengerService passengerService;

    public testReportController(ReservationService reservationService, ReportService reportService, PassengerService passengerService) {
        this.reservationService = reservationService;
        this.reportService = reportService;
        this.passengerService = passengerService;
    }
}

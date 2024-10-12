package com.project.seatReservation.controller.reports;

import com.project.seatReservation.model.Passenger;
import com.project.seatReservation.model.Reservation;
import com.project.seatReservation.model.SeatReservation;
import com.project.seatReservation.model.report.PassengerReservationReportTemplate;
import com.project.seatReservation.service.PassengerService;
import com.project.seatReservation.service.ReportService;
import com.project.seatReservation.service.ReservationService;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@CrossOrigin
public class PassengerReservationReportController {

    ReservationService reservationService;
    ReportService reportService;
    PassengerService passengerService;

    public PassengerReservationReportController(ReservationService reservationService,ReportService reportService,PassengerService passengerService) {
        this.reservationService = reservationService;
        this.reportService = reportService;
        this.passengerService = passengerService;
    }

    @RequestMapping(value = "/generateUserReservationHistoryReport", method = RequestMethod.GET)
    public ResponseEntity<byte[]> generateUserReservationHistoryReport(@RequestParam("userId") Integer userId,@RequestParam("fromDate") String fromDateStr,@RequestParam("toDate") String toDateStr){

        try{
            String jrxmlTemplateName = "PassengerReservationReport.jrxml";

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date fromDate = null;
            Date toDate = null;
            try {
                fromDate = sdf.parse(fromDateStr);
                toDate = sdf.parse(toDateStr);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            List<Reservation> reservationList = reservationService.findReservationsByUserIdAndDateRange(userId,fromDate,toDate);

            List<PassengerReservationReportTemplate> reportTemplateList = new ArrayList<>();

            for(Reservation r : reservationList){
                PassengerReservationReportTemplate template = new PassengerReservationReportTemplate();
                template.setTripDate(r.getSchedule().getTripDateStr());
                template.setTripOrigin(r.getSchedule().getOrigin());
                template.setTripDestination(r.getSchedule().getDestination());
                template.setTripStartTime(r.getSchedule().getTripStartTime());

                List<SeatReservation> seatReservationList = reservationService.findSuccessReservedSeatsByRevId(r.getReservationId());
                template.setNoOfSeats(seatReservationList.size());

                double ticketPrice =0;
                double discount = 0;

                for(SeatReservation sr: seatReservationList){
                    ticketPrice +=sr.getTicketPriceAfterDis();
                    discount +=sr.getDiscountAmount();
                }

                template.setTicketPrice(ticketPrice);
                template.setDiscountAmount(discount);

                reportTemplateList.add(template);
            }

            JRDataSource dataSource = new JRBeanCollectionDataSource(reportTemplateList);
            Map<String,Object> parameters = new HashMap<>();

            List<Passenger> passengers = passengerService.findPassengerByUserId(userId);
            if(passengers.size() >0){
                parameters.put("passengerName",passengers.get(0).getName());
            }else{
                parameters.put("passengerName","");
            }

            String generatedDate = sdf.format(new Date());
            parameters.put("generatedDate",generatedDate);
            parameters.put("fromDate",fromDateStr);
            parameters.put("toDate",toDateStr);

            byte[] pdfBytes = reportService.generatePDF(jrxmlTemplateName,dataSource,parameters);


            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "PassengerReservationReport.pdf");

            // Return the PDF file as a byte array
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        }catch (IOException | JRException e) {

            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }



    }
}

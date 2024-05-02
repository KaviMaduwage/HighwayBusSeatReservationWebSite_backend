package com.project.seatReservation.controller.reports;

import com.project.seatReservation.model.BusCrew;
import com.project.seatReservation.model.BusOwner;
import com.project.seatReservation.model.report.BusOwnerListReportTemplate;
import com.project.seatReservation.service.BusCrewService;
import com.project.seatReservation.service.BusOwnerService;
import com.project.seatReservation.service.ReportService;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import net.sf.jasperreports.engine.*;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@CrossOrigin
public class BusOwnersListReportController {
    ReportService reportService;
    BusOwnerService busOwnerService;

    BusCrewService busCrewService;

    public BusOwnersListReportController(ReportService reportService,BusOwnerService busOwnerService,BusCrewService busCrewService) {
        this.reportService = reportService;
        this.busOwnerService = busOwnerService;
        this.busCrewService = busCrewService;
    }


    @RequestMapping(value = "/generateBusOwnerListReport",method = RequestMethod.GET)
    public ResponseEntity<byte[]> generateReport() {
        try {
            // Generate empty PDF using JasperReports
            String jrxmlTemplateName = "BusOwnersListReport.jrxml";

            List<BusOwnerListReportTemplate> reportTemplateList = new ArrayList<>();
            List<BusOwner> busOwnerList = busOwnerService.findActiveBusOwnerList();

            for(BusOwner busOwner : busOwnerList){
                BusOwnerListReportTemplate template = new BusOwnerListReportTemplate();

                List<BusCrew> driverList = busCrewService.getDriverListByBusOwnerId(busOwner.getBusOwnerId());
                List<BusCrew> conductorList = busCrewService.getConductorListByBusOwnerId(busOwner.getBusOwnerId());

                template.setTravelServiceName(busOwner.getTravelServiceName());
                template.setBusOwnerName(busOwner.getOwnerName());
                template.setAddress(busOwner.getAddress());
                template.setTel(busOwner.getMobileNo());
                template.setNoOfConductors(conductorList.size());
                template.setNoOfDrivers(driverList.size());

                reportTemplateList.add(template);
            }

            JRDataSource dataSource = new JRBeanCollectionDataSource(reportTemplateList);
            Map<String,Object> parameters = new HashMap<>();

            SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
            String generatedDate = sdf.format(new Date());
            parameters.put("generatedDate",generatedDate);

            byte[] pdfBytes = reportService.generatePDF(jrxmlTemplateName,dataSource,parameters);


            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "BusOwnersListReport.pdf");

            // Return the PDF file as a byte array
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (IOException | JRException e) {

            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

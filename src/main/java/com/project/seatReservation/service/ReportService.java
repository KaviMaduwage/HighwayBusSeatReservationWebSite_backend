package com.project.seatReservation.service;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReportService {

    public byte[] generatePDF(String jrxmlTemplateName, JRDataSource dataSource, Map<String, Object> parameters) throws JRException, IOException {

        String resourceLocation = "classpath:jrxml/"+jrxmlTemplateName;
        File file = ResourceUtils.getFile(resourceLocation);
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());


        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parameters,dataSource);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);

        return outputStream.toByteArray();
    }
}

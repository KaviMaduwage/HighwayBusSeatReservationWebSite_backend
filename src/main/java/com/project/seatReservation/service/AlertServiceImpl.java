package com.project.seatReservation.service;

import com.project.seatReservation.dao.AlertDao;
import com.project.seatReservation.dao.AlertTypeDao;
import com.project.seatReservation.model.Alert;
import com.project.seatReservation.model.AlertType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AlertServiceImpl implements AlertService{
    AlertTypeDao alertTypeDao;

    AlertDao alertDao;

    public AlertServiceImpl(AlertTypeDao alertTypeDao, AlertDao alertDao) {
        this.alertTypeDao = alertTypeDao;
        this.alertDao = alertDao;
    }


    @Override
    public List<AlertType> getAlertTypeList() {
        return alertTypeDao.findAll();
    }

    @Override
    public void saveAlert(Alert alert) {
        alertDao.save(alert);
    }

    @Override
    public List<Alert> getAllAlertsByUserTypeId(int userTypeId, Date pastDate) {
        List<Alert> alerts = new ArrayList<>();
        if(userTypeId == 1){
            //admin
            alerts = alertDao.getAllPrevious10DaysAlerts(pastDate);
        }else if(userTypeId == 2){
            //bus owner
            alerts = alertDao.getAllAlertsForBusOwnerForPrevious10Days(pastDate);
        }else{
            alerts = alertDao.getAllAlertsForOtherUsersForPrevious10Days(pastDate);

        }
        return alerts;
    }
}

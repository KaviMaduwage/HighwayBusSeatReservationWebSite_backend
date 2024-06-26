package com.project.seatReservation.service;

import com.project.seatReservation.model.Alert;
import com.project.seatReservation.model.AlertType;

import java.util.Date;
import java.util.List;

public interface AlertService {
    List<AlertType> getAlertTypeList();

    void saveAlert(Alert alert);

    List<Alert> getAllAlertsByUserTypeId(int userTypeId, Date pastDate);
}

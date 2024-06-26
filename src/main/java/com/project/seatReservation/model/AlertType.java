package com.project.seatReservation.model;

import javax.persistence.*;

@Entity
@Table(name = "alert_type")
public class AlertType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int alertTypeId;

    private String description;

    public int getAlertTypeId() {
        return alertTypeId;
    }

    public void setAlertTypeId(int alertTypeId) {
        this.alertTypeId = alertTypeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

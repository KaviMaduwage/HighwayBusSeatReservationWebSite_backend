package com.project.seatReservation.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "alert")
public class Alert {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int alertId;

    private String reason;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    private Date createdDate;

    private boolean forAll;
    private boolean forAdmin;
    private boolean forTravelService;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @ManyToOne
    @JoinColumn(name = "alert_type_id")
    private AlertType alertType;

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public int getAlertId() {
        return alertId;
    }

    public void setAlertId(int alertId) {
        this.alertId = alertId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public boolean isForAll() {
        return forAll;
    }

    public void setForAll(boolean forAll) {
        this.forAll = forAll;
    }

    public boolean isForAdmin() {
        return forAdmin;
    }

    public void setForAdmin(boolean forAdmin) {
        this.forAdmin = forAdmin;
    }

    public boolean isForTravelService() {
        return forTravelService;
    }

    public void setForTravelService(boolean forTravelService) {
        this.forTravelService = forTravelService;
    }

    public AlertType getAlertType() {
        return alertType;
    }

    public void setAlertType(AlertType alertType) {
        this.alertType = alertType;
    }
}

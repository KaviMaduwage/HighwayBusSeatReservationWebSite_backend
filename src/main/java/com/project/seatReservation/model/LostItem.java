package com.project.seatReservation.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "lost_item")
public class LostItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int lostItemId;

    private String reporterName;
    private Date reportedDate;

    private String contactNo;

    private Date incidentDate;
    private String incidentTime;
    private String scheduleTime;
    private String scheduleOrigin;
    private String scheduleDestination;
    private String itemName;
    private String itemColor;
    private String busNo;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public String getBusNo() {
        return busNo;
    }

    public void setBusNo(String busNo) {
        this.busNo = busNo;
    }

    public int getLostItemId() {
        return lostItemId;
    }

    public void setLostItemId(int lostItemId) {
        this.lostItemId = lostItemId;
    }

    public String getReporterName() {
        return reporterName;
    }

    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }

    public Date getReportedDate() {
        return reportedDate;
    }

    public void setReportedDate(Date reportedDate) {
        this.reportedDate = reportedDate;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public Date getIncidentDate() {
        return incidentDate;
    }

    public void setIncidentDate(Date incidentDate) {
        this.incidentDate = incidentDate;
    }

    public String getIncidentTime() {
        return incidentTime;
    }

    public void setIncidentTime(String incidentTime) {
        this.incidentTime = incidentTime;
    }

    public String getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(String scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public String getScheduleOrigin() {
        return scheduleOrigin;
    }

    public void setScheduleOrigin(String scheduleOrigin) {
        this.scheduleOrigin = scheduleOrigin;
    }

    public String getScheduleDestination() {
        return scheduleDestination;
    }

    public void setScheduleDestination(String scheduleDestination) {
        this.scheduleDestination = scheduleDestination;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemColor() {
        return itemColor;
    }

    public void setItemColor(String itemColor) {
        this.itemColor = itemColor;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

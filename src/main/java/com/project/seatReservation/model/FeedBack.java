package com.project.seatReservation.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "feedback")
public class FeedBack {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int feedBackId;
    private Date travelDate;

    @ManyToOne
    @JoinColumn(name = "bus_id")
    private Bus bus;
    private int cleanlinessGradeId;
    private int timingGradeId;
    private int driverGradeId;
    private int conductorGradeId;
    private String otherComments;

    private Date createdDate;

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getFeedBackId() {
        return feedBackId;
    }

    public void setFeedBackId(int feedBackId) {
        this.feedBackId = feedBackId;
    }

    public Date getTravelDate() {
        return travelDate;
    }

    public void setTravelDate(Date travelDate) {
        this.travelDate = travelDate;
    }

    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    public int getCleanlinessGradeId() {
        return cleanlinessGradeId;
    }

    public void setCleanlinessGradeId(int cleanlinessGradeId) {
        this.cleanlinessGradeId = cleanlinessGradeId;
    }

    public int getTimingGradeId() {
        return timingGradeId;
    }

    public void setTimingGradeId(int timingGradeId) {
        this.timingGradeId = timingGradeId;
    }

    public int getDriverGradeId() {
        return driverGradeId;
    }

    public void setDriverGradeId(int driverGradeId) {
        this.driverGradeId = driverGradeId;
    }

    public int getConductorGradeId() {
        return conductorGradeId;
    }

    public void setConductorGradeId(int conductorGradeId) {
        this.conductorGradeId = conductorGradeId;
    }

    public String getOtherComments() {
        return otherComments;
    }

    public void setOtherComments(String otherComments) {
        this.otherComments = otherComments;
    }
}

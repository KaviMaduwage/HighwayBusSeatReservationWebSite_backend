package com.project.seatReservation.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "busOwner")
public class BusOwner {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int busOwnerId;

    private String ownerName;
    private String travelServiceName;
    private String address;
    private String mobileNo;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public int getBusOwnerId() {
        return busOwnerId;
    }

    public void setBusOwnerId(int busOwnerId) {
        this.busOwnerId = busOwnerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getTravelServiceName() {
        return travelServiceName;
    }

    public void setTravelServiceName(String travelServiceName) {
        this.travelServiceName = travelServiceName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

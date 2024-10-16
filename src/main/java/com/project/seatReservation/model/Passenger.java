package com.project.seatReservation.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "passenger")
public class Passenger {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int passengerId;

    private String name;
    private String address;
    private String gender;
    private String mobileNo;
    private Date dob;
    private String occupation;

    private boolean emailSubscription;
    private boolean messageSubscription;

    public boolean isEmailSubscription() {
        return emailSubscription;
    }

    public void setEmailSubscription(boolean emailSubscription) {
        this.emailSubscription = emailSubscription;
    }

    public boolean isMessageSubscription() {
        return messageSubscription;
    }

    public void setMessageSubscription(boolean messageSubscription) {
        this.messageSubscription = messageSubscription;
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String nic;

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public int getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(int passengerId) {
        this.passengerId = passengerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

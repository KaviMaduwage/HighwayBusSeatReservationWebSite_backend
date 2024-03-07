package com.project.seatReservation.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Entity
@Table(name = "bus_crew")
public class BusCrew {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int busCrewId;
    private String name;
    private String address;
    private String gender;
    private String mobileNo;
    private Date dob;
    private String photoURL;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public int getBusCrewId() {
        return busCrewId;
    }

    public void setBusCrewId(int busCrewId) {
        this.busCrewId = busCrewId;
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

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public int getAge(){

        if(dob == null){
            return 0;
        }

        Date currentDate = new Date();

        LocalDate db = dob.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate now = LocalDate.now();

        int age = now.getYear() - db.getYear();
        if(now.getMonthValue() < db.getMonthValue()){
            age--;
        }

        return age;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

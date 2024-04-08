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

    private String mobileNo;
    private Date dob;
    private String nic;
    private String profileImage;
    private String licenseNo;
    private Date expiryDate;
    private Date issuesDate;

    @ManyToOne
    @JoinColumn(name = "bus_owner_id")
    private BusOwner busOwner;


    @ManyToOne
    @JoinColumn(name = "bus_crew_type_id")
    private BusCrewType busCrewType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private int ntcNo;
    private String status;

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

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
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

    public BusCrewType getBusCrewType() {
        return busCrewType;
    }

    public void setBusCrewType(BusCrewType busCrewType) {
        this.busCrewType = busCrewType;
    }

    public int getNtcNo() {
        return ntcNo;
    }

    public void setNtcNo(int ntcNo) {
        this.ntcNo = ntcNo;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLicenseNo() {
        return licenseNo;
    }

    public void setLicenseNo(String licenseNo) {
        this.licenseNo = licenseNo;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Date getIssuesDate() {
        return issuesDate;
    }

    public void setIssuesDate(Date issuesDate) {
        this.issuesDate = issuesDate;
    }

    public BusOwner getBusOwner() {
        return busOwner;
    }

    public void setBusOwner(BusOwner busOwner) {
        this.busOwner = busOwner;
    }
}

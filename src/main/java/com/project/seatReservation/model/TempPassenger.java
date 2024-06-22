package com.project.seatReservation.model;

import javax.persistence.*;

@Entity
@Table(name = "temp_passenger")
public class TempPassenger {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int tempPassengerId;

    private String nic;
    private String mobileNo;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public int getTempPassengerId() {
        return tempPassengerId;
    }

    public void setTempPassengerId(int tempPassengerId) {
        this.tempPassengerId = tempPassengerId;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
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

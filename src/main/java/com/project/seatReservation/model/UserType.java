package com.project.seatReservation.model;

import javax.persistence.*;

@Entity
@Table(name = "userType")
public class UserType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int userTypeId;
    private String description;

    public int getUserTypeId() {
        return userTypeId;
    }

    public void setUserTypeId(int userTypeId) {
        this.userTypeId = userTypeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

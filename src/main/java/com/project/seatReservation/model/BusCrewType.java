package com.project.seatReservation.model;

import javax.persistence.*;

@Entity
@Table(name = "bus_crew_type")
public class BusCrewType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int busCrewTypeId;

    private String description;

    public int getBusCrewTypeId() {
        return busCrewTypeId;
    }

    public void setBusCrewTypeId(int busCrewTypeId) {
        this.busCrewTypeId = busCrewTypeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

package com.project.seatReservation.model;

import javax.persistence.*;

@Entity
@Table(name = "trip_crew")
public class TripCrew {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int tripCrewId;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @ManyToOne
    @JoinColumn(name = "bus_crew_id")
    private BusCrew busCrew;

    public int getTripCrewId() {
        return tripCrewId;
    }

    public void setTripCrewId(int tripCrewId) {
        this.tripCrewId = tripCrewId;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public BusCrew getBusCrew() {
        return busCrew;
    }

    public void setBusCrew(BusCrew busCrew) {
        this.busCrew = busCrew;
    }
}

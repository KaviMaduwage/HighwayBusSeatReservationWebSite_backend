package com.project.seatReservation.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int reservationId;
    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @ManyToOne
    @JoinColumn(name = "passenger_id")
    private Passenger passenger;

    private boolean isCancelled;
    private Date reserveDate;
    private String boardingPoint;
    private String droppingPoint;

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }

    public Date getReserveDate() {
        return reserveDate;
    }

    public void setReserveDate(Date reserveDate) {
        this.reserveDate = reserveDate;
    }

    public String getBoardingPoint() {
        return boardingPoint;
    }

    public void setBoardingPoint(String boardingPoint) {
        this.boardingPoint = boardingPoint;
    }

    public String getDroppingPoint() {
        return droppingPoint;
    }

    public void setDroppingPoint(String droppingPoint) {
        this.droppingPoint = droppingPoint;
    }
}

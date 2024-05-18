package com.project.seatReservation.model;

import javax.persistence.*;

@Entity
@Table(name = "seat_reservation")
public class SeatReservation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int seatReservationId;

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @ManyToOne
    @JoinColumn(name = "seat_id")
    private Seat seat;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getSeatReservationId() {
        return seatReservationId;
    }

    public void setSeatReservationId(int seatReservationId) {
        this.seatReservationId = seatReservationId;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public Seat getSeat() {
        return seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }
}

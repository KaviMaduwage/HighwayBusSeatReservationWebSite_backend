package com.project.seatReservation.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "notify_seat_cancellation")
public class NotifySeatCancellation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int notifySeatCancellationId;

    @ManyToOne
    @JoinColumn(name = "passenger_id")
    private Passenger passenger;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    private Date createdDate;

    public int getNotifySeatCancellationId() {
        return notifySeatCancellationId;
    }

    public void setNotifySeatCancellationId(int notifySeatCancellationId) {
        this.notifySeatCancellationId = notifySeatCancellationId;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}

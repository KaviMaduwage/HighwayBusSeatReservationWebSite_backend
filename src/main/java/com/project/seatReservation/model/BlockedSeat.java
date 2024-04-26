package com.project.seatReservation.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "blocked_seat")
public class BlockedSeat {
    public BlockedSeat() {
        this.modifiedTime = Timestamp.valueOf(LocalDateTime.now());
    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int blockedSeatId;
    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private int row;
    private int col;

    private Timestamp modifiedTime;

    public int getBlockedSeatId() {
        return blockedSeatId;
    }

    public void setBlockedSeatId(int blockedSeatId) {
        this.blockedSeatId = blockedSeatId;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }
}

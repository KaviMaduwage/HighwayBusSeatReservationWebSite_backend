package com.project.seatReservation.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "cart")
public class Cart {

    public Cart() {
        this.modifiedTime = Timestamp.valueOf(LocalDateTime.now());
    }

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private int cartId;
    private String pickUpPoint;
    private String dropPoint;
    private String remark;
    private double price;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;
    private String blockedSeatIds;
    private String seatNos;
    private Timestamp modifiedTime;

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }


    public String getPickUpPoint() {
        return pickUpPoint;
    }

    public void setPickUpPoint(String pickUpPoint) {
        this.pickUpPoint = pickUpPoint;
    }

    public String getDropPoint() {
        return dropPoint;
    }

    public void setDropPoint(String dropPoint) {
        this.dropPoint = dropPoint;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getBlockedSeatIds() {
        return blockedSeatIds;
    }

    public void setBlockedSeatIds(String blockedSeatIds) {
        this.blockedSeatIds = blockedSeatIds;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public String getSeatNos() {
        return seatNos;
    }

    public void setSeatNos(String seatNos) {
        this.seatNos = seatNos;
    }
}

package com.project.seatReservation.model.report;

import com.project.seatReservation.model.Discount;

import java.util.List;

public class TicketTemplate {
    private String description;
    private String busNo;
    private String origin;
    private String destination;
    private String seatNos;
    private String travelDate;
    private String travelTime;
    private String price;
    private String nic;
    private String discountAmount;
    private String discountDes;

    private String amountAfterDiscount;

    public String getAmountAfterDiscount() {
        return amountAfterDiscount;
    }

    public void setAmountAfterDiscount(String amountAfterDiscount) {
        this.amountAfterDiscount = amountAfterDiscount;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getDiscountDes() {
        return discountDes;
    }

    public void setDiscountDes(String discountDes) {
        this.discountDes = discountDes;
    }

    public String getBusNo() {
        return busNo;
    }

    public void setBusNo(String busNo) {
        this.busNo = busNo;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getSeatNos() {
        return seatNos;
    }

    public void setSeatNos(String seatNos) {
        this.seatNos = seatNos;
    }

    public String getTravelDate() {
        return travelDate;
    }

    public void setTravelDate(String travelDate) {
        this.travelDate = travelDate;
    }

    public String getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(String travelTime) {
        this.travelTime = travelTime;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

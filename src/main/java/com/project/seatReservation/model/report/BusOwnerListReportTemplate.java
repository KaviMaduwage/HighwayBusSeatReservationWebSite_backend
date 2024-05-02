package com.project.seatReservation.model.report;

public class BusOwnerListReportTemplate {
    private String travelServiceName;
    private String busOwnerName;
    private String address;
    private String tel;
    private int noOfDrivers;
    private int noOfConductors;

    public String getTravelServiceName() {
        return travelServiceName;
    }

    public void setTravelServiceName(String travelServiceName) {
        this.travelServiceName = travelServiceName;
    }

    public String getBusOwnerName() {
        return busOwnerName;
    }

    public void setBusOwnerName(String busOwnerName) {
        this.busOwnerName = busOwnerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public int getNoOfDrivers() {
        return noOfDrivers;
    }

    public void setNoOfDrivers(int noOfDrivers) {
        this.noOfDrivers = noOfDrivers;
    }

    public int getNoOfConductors() {
        return noOfConductors;
    }

    public void setNoOfConductors(int noOfConductors) {
        this.noOfConductors = noOfConductors;
    }
}

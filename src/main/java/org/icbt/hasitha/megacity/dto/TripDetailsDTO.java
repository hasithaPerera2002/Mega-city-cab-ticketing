package org.icbt.hasitha.megacity.dto;

import org.icbt.hasitha.megacity.util.enums.VehicleTypes;

import java.util.Date;

public class TripDetailsDTO {

    private String tripId;
    private String vehicleType;
    private String customerEmail;
    private long date;
    private String destination;
    private String startLocation;
    private String startTime;
    private String fare;
    private String distance;
    private int passengerCount;


    public TripDetailsDTO() {
    }

    public TripDetailsDTO(String tripId, String vehicleType, String customerEmail, long date, String destination, String startTime, String fare, String distance,  int passengerCount, String startLocation) {
        this.tripId = tripId;
        this.vehicleType = vehicleType;
        this.customerEmail = customerEmail;
        this.date = date;
        this.destination = destination;
        this.startTime = startTime;
        this.fare = fare;
        this.distance = distance;
        this.startLocation = startLocation;
        this.passengerCount = passengerCount;
    }

    public TripDetailsDTO(String vehicleType, String customerEmail, int date, String destination, String startTime, String fare, String distance, int passengerCount, String startLocation) {
        this.vehicleType = vehicleType;
        this.customerEmail = customerEmail;
        this.date = date;
        this.destination = destination;
        this.startTime = startTime;
        this.fare = fare;
        this.distance = distance;
        this.passengerCount = passengerCount;
        this.startLocation = startLocation;
    }


    @Override
    public String toString() {
        return "TripDetailsDTO{" +
                "tripId='" + tripId + '\'' +
                ", vehicleType='" + vehicleType + '\'' +
                ", customerEmail='" + customerEmail + '\'' +
                ", date=" + date +
                ", destination='" + destination + '\'' +
                ", startTime='" + startTime + '\'' +
                ", fare='" + fare + '\'' +
                ", distance='" + distance + '\'' +
                ", passengerCount=" + passengerCount +
                '}';
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getFare() {
        return fare;
    }

    public void setFare(String fare) {
        this.fare = fare;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public int getPassengerCount() {
        return passengerCount;
    }

    public void setPassengerCount(int passengerCount) {
        this.passengerCount = passengerCount;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }


}

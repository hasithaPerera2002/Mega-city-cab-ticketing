package org.icbt.hasitha.megacity.entity;

import java.util.Date;

public class Trip {

    private String tripId;
    private String vehicleId;
    private String customerId;
    private long date;
    private String destination;
    private String startLocation;
    private String startTime;
    private String fare;
    private String distance;
    private int passengerCount;

    public Trip() {
    }


    public Trip(String tripId, String vehicleId, String customerId, long date, String destination, String startLocation, String startTime, String fare, String distance, int passengerCount) {
        this.tripId = tripId;
        this.vehicleId = vehicleId;
        this.customerId = customerId;
        this.date = date;
        this.destination = destination;
        this.startLocation = startLocation;
        this.startTime = startTime;
        this.fare = fare;
        this.distance = distance;
        this.passengerCount = passengerCount;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
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

    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
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

    @Override
    public String toString() {
        return "Trip{" +
                "tripId='" + tripId + '\'' +
                ", vehicleId='" + vehicleId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", date=" + date +
                ", destination='" + destination + '\'' +
                ", startLocation='" + startLocation + '\'' +
                ", startTime='" + startTime + '\'' +
                ", fare='" + fare + '\'' +
                ", distance='" + distance + '\'' +
                ", passengerCount=" + passengerCount +
                '}';
    }
}

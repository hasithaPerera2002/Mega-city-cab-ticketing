package dto;

import java.util.Date;

public class TripDetailsDTO {
    private String tripId;
    private String vehicleId;
    private String customerId;
    private Date date;
    private String destination;
    private String startTime;
    private String endTime;
    private String fare;
    private String distance;
    private String startLongitude;
    private String startLatitude;
    private String endLongitude;
    private String endLatitude;
    public TripDetailsDTO() {
    }

    public TripDetailsDTO(String tripId, String vehicleId, String customerId, Date date, String destination, String startTime, String endTime, String fare, String distance, String startLongitude, String startLatitude, String endLongitude, String endLatitude) {
        this.tripId = tripId;
        this.vehicleId = vehicleId;
        this.customerId = customerId;
        this.date = date;
        this.destination = destination;
        this.startTime = startTime;
        this.endTime = endTime;
        this.fare = fare;
        this.distance = distance;
        this.startLongitude = startLongitude;
        this.startLatitude = startLatitude;
        this.endLongitude = endLongitude;
        this.endLatitude = endLatitude;
    }


    @Override
    public String toString() {
        return "TripDetailsDTO{" +
                "tripId='" + tripId + '\'' +
                ", vehicleId='" + vehicleId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", date=" + date +
                ", destination='" + destination + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", fare='" + fare + '\'' +
                ", distance='" + distance + '\'' +
                ", startLongitude='" + startLongitude + '\'' +
                ", startLatitude='" + startLatitude + '\'' +
                ", endLongitude='" + endLongitude + '\'' +
                ", endLatitude='" + endLatitude + '\'' +
                '}';
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
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

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
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

    public String getStartLongitude() {
        return startLongitude;
    }

    public void setStartLongitude(String startLongitude) {
        this.startLongitude = startLongitude;
    }

    public String getStartLatitude() {
        return startLatitude;
    }

    public void setStartLatitude(String startLatitude) {
        this.startLatitude = startLatitude;
    }

    public String getEndLongitude() {
        return endLongitude;
    }

    public void setEndLongitude(String endLongitude) {
        this.endLongitude = endLongitude;
    }

    public String getEndLatitude() {
        return endLatitude;
    }

    public void setEndLatitude(String endLatitude) {
        this.endLatitude = endLatitude;
    }


}

package entity;

public class Trip {

    private Long trip_id;
    private String customer_id;
    private String vehicle_id;
    private String date;
    private String start_longitude;
    private String start_latitude;
    private String end_longitude;
    private String end_latitude;
    private int fare;

    public Trip() {
    }

    public Trip(Long trip_id, String customer_id, String vehicle_id, String date, String start_longitude, String start_latitude, String end_longitude, String end_latitude, int fare) {
        this.trip_id = trip_id;
        this.customer_id = customer_id;
        this.vehicle_id = vehicle_id;
        this.date = date;
        this.start_longitude = start_longitude;
        this.start_latitude = start_latitude;
        this.end_longitude = end_longitude;
        this.end_latitude = end_latitude;
        this.fare = fare;
    }

    public Long getTrip_id() {
        return trip_id;
    }

    public void setTrip_id(Long trip_id) {
        this.trip_id = trip_id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getVehicle_id() {
        return vehicle_id;
    }

    public void setVehicle_id(String vehicle_id) {
        this.vehicle_id = vehicle_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStart_longitude() {
        return start_longitude;
    }

    public void setStart_longitude(String start_longitude) {
        this.start_longitude = start_longitude;
    }

    public String getStart_latitude() {
        return start_latitude;
    }

    public void setStart_latitude(String start_latitude) {
        this.start_latitude = start_latitude;
    }

    public String getEnd_longitude() {
        return end_longitude;
    }

    public void setEnd_longitude(String end_longitude) {
        this.end_longitude = end_longitude;
    }

    public String getEnd_latitude() {
        return end_latitude;
    }

    public void setEnd_latitude(String end_latitude) {
        this.end_latitude = end_latitude;
    }

    public int getFare() {
        return fare;
    }

    public void setFare(int fare) {
        this.fare = fare;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "trip_id=" + trip_id +
                ", customer_id='" + customer_id + '\'' +
                ", vehicle_id='" + vehicle_id + '\'' +
                ", date='" + date + '\'' +
                ", start_longitude='" + start_longitude + '\'' +
                ", start_latitude='" + start_latitude + '\'' +
                ", end_longitude='" + end_longitude + '\'' +
                ", end_latitude='" + end_latitude + '\'' +
                ", fare=" + fare +
                '}';
    }
}

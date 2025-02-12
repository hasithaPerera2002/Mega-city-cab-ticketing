package entity;

import java.util.UUID;

public class Vehicle {

    private UUID vehicle_id;
    private String vehicle_number;
    private String vehicle_type;
    private String vehicle_color;
    private String vehicle_modal;
    private String vehicle_image;
    private String status;
    private UUID driver_id;
    private String driver_name;
    private String driver_contact;
    private String driver_nic;
    private String driver_email;

    public Vehicle() {
    }

    public Vehicle(UUID vehicle_id, String vehicle_number, String vehicle_type, String vehicle_color, String vehicle_modal, String vehicle_image, String status, UUID driver_id, String driver_name, String driver_contact, String driver_nic, String driver_email) {
        this.vehicle_id = vehicle_id;
        this.vehicle_number = vehicle_number;
        this.vehicle_type = vehicle_type;
        this.vehicle_color = vehicle_color;
        this.vehicle_modal = vehicle_modal;
        this.vehicle_image = vehicle_image;
        this.status = status;
        this.driver_id = driver_id;
        this.driver_name = driver_name;
        this.driver_contact = driver_contact;
        this.driver_nic = driver_nic;
        this.driver_email = driver_email;
    }

    public UUID getVehicle_id() {
        return vehicle_id;
    }

    public void setVehicle_id(UUID vehicle_id) {
        this.vehicle_id = vehicle_id;
    }

    public String getVehicle_number() {
        return vehicle_number;
    }

    public void setVehicle_number(String vehicle_number) {
        this.vehicle_number = vehicle_number;
    }

    public String getVehicle_type() {
        return vehicle_type;
    }

    public void setVehicle_type(String vehicle_type) {
        this.vehicle_type = vehicle_type;
    }

    public String getVehicle_color() {
        return vehicle_color;
    }

    public void setVehicle_color(String vehicle_color) {
        this.vehicle_color = vehicle_color;
    }

    public String getVehicle_modal() {
        return vehicle_modal;
    }

    public void setVehicle_modal(String vehicle_modal) {
        this.vehicle_modal = vehicle_modal;
    }

    public String getVehicle_image() {
        return vehicle_image;
    }

    public void setVehicle_image(String vehicle_image) {
        this.vehicle_image = vehicle_image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UUID getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(UUID driver_id) {
        this.driver_id = driver_id;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    public String getDriver_contact() {
        return driver_contact;
    }

    public void setDriver_contact(String driver_contact) {
        this.driver_contact = driver_contact;
    }

    public String getDriver_nic() {
        return driver_nic;
    }

    public void setDriver_nic(String driver_nic) {
        this.driver_nic = driver_nic;
    }

    public String getDriver_email() {
        return driver_email;
    }

    public void setDriver_email(String driver_email) {
        this.driver_email = driver_email;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "vehicle_id=" + vehicle_id +
                ", vehicle_number='" + vehicle_number + '\'' +
                ", vehicle_type='" + vehicle_type + '\'' +
                ", vehicle_color='" + vehicle_color + '\'' +
                ", vehicle_modal='" + vehicle_modal + '\'' +
                ", vehicle_image='" + vehicle_image + '\'' +
                ", status='" + status + '\'' +
                ", driver_id='" + driver_id + '\'' +
                ", driver_name='" + driver_name + '\'' +
                ", driver_contact='" + driver_contact + '\'' +
                ", driver_nic='" + driver_nic + '\'' +
                ", driver_email='" + driver_email + '\'' +
                '}';
    }


}

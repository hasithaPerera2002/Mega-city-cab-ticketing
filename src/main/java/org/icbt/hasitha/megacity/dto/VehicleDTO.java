package org.icbt.hasitha.megacity.dto;


import org.icbt.hasitha.megacity.util.enums.VehicleStatus;
import org.icbt.hasitha.megacity.util.enums.VehicleTypes;

import java.util.UUID;

public class VehicleDTO {
    private UUID vehicle_id;
    private String vehicle_number;
    private VehicleTypes vehicle_type;
    private VehicleStatus status;
    private UUID driver_id;
    private String driver_name;
    private String driver_contact;
    private String driver_nic;
    private String driver_email;

    public VehicleDTO() {
    }

    public VehicleDTO(UUID vehicle_id, String vehicle_number, VehicleTypes vehicle_type,  VehicleStatus status, UUID driver_id, String driver_name, String driver_contact, String driver_nic, String driver_email) {
        this.vehicle_id = vehicle_id;
        this.vehicle_number = vehicle_number;
        this.vehicle_type = vehicle_type;
        this.status = status;
        this.driver_id = driver_id;
        this.driver_name = driver_name;
        this.driver_contact = driver_contact;
        this.driver_nic = driver_nic;
        this.driver_email = driver_email;
    }

    public VehicleDTO(UUID vehicleId, String vehicleNumber, VehicleTypes vehicleType, VehicleStatus status, UUID driverId, String driverName, String driverContact, String driverNic, String driverEmail, Object o) {

        this.vehicle_id = vehicleId;
        this.vehicle_number = vehicleNumber;

        this.vehicle_type = vehicleType;
        this.status = status;
        this.driver_id = driverId;
        this.driver_name = driverName;
        this.driver_contact = driverContact;
        this.driver_nic = driverNic;
        this.driver_email = driverEmail;


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

    public VehicleTypes getVehicle_type() {
        return vehicle_type;
    }

    public void setVehicle_type(VehicleTypes vehicle_type) {
        this.vehicle_type = vehicle_type;
    }

    public VehicleStatus getStatus() {
        return status;
    }

    public void setStatus(VehicleStatus status) {
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
        return "VehicleDTO{" +
                "vehicle_id=" + vehicle_id +
                ", vehicle_number='" + vehicle_number + '\'' +
                ", vehicle_type=" + vehicle_type +
                ", status=" + status +
                ", driver_id=" + driver_id +
                ", driver_name='" + driver_name + '\'' +
                ", driver_contact='" + driver_contact + '\'' +
                ", driver_nic='" + driver_nic + '\'' +
                ", driver_email='" + driver_email + '\'' +
                '}';
    }


}

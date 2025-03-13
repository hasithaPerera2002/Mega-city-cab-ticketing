package org.icbt.hasitha.megacity.repository;

import org.icbt.hasitha.megacity.dto.VehicleDTO;
import org.icbt.hasitha.megacity.entity.Vehicle;
import org.icbt.hasitha.megacity.util.db.DBConnection;
import org.icbt.hasitha.megacity.util.enums.VehicleTypes;
import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class VehicleRepo {
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(VehicleRepo.class);
    public boolean addVehicle(VehicleDTO vehicleDTO) {
        LOGGER.info("Adding new vehicle...");

        String query = "INSERT INTO vehicles (vehicle_id, vehicle_number, vehicle_type,  status, driver_id, driver_name, driver_contact, driver_nic, driver_email) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setObject(1, UUID.randomUUID());  // Generate unique vehicle ID
            stmt.setString(2, vehicleDTO.getVehicle_number());
            stmt.setString(3, vehicleDTO.getVehicle_type().getDisplayName());
            stmt.setString(4, "AVAILABLE");
            stmt.setObject(5, vehicleDTO.getDriver_id());
            stmt.setString(6, vehicleDTO.getDriver_name());
            stmt.setString(7, vehicleDTO.getDriver_contact());
            stmt.setString(8, vehicleDTO.getDriver_nic());
            stmt.setString(9, vehicleDTO.getDriver_email());
            int rowsAffected = stmt.executeUpdate();
            LOGGER.info("Vehicle added successfully");
            return rowsAffected > 0;

        } catch (SQLException e) {
            LOGGER.error("Error adding vehicle", e);
            throw new RuntimeException("Failed to add vehicle", e);
        }

    }

    public void updateVehicle() {
        // update vehicle
    }

    public void deleteVehicle() {
        // delete vehicle
    }

    public void getVehicle() {
        // get vehicle
    }

    public UUID getVehicleIdByType(VehicleTypes vehicleType) {
        String query = "SELECT vehicle_id FROM vehicles WHERE vehicle_type = ? AND status = 'AVAILABLE' LIMIT 1";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, vehicleType.name()); // Set enum name as string

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return UUID.fromString(rs.getString("vehicle_id"));
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error fetching vehicle ID by type", e);
        }
        return null; // Return null if no available vehicle is found
    }


    public void getAllVehicles() {
        // get all vehicles
    }
}

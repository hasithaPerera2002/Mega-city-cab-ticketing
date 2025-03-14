package org.icbt.hasitha.megacity.repository;

import org.icbt.hasitha.megacity.dto.VehicleDTO;
import org.icbt.hasitha.megacity.entity.Vehicle;
import org.icbt.hasitha.megacity.repository.interfaces.IVehicleRepo;
import org.icbt.hasitha.megacity.util.db.DBConnection;
import org.icbt.hasitha.megacity.util.enums.VehicleStatus;
import org.icbt.hasitha.megacity.util.enums.VehicleTypes;
import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.UUID;

public class VehicleRepo implements IVehicleRepo {
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(VehicleRepo.class);

    @Override
    public boolean addVehicle(VehicleDTO vehicleDTO) {
        LOGGER.info("Adding new vehicle...");

        String query = "INSERT INTO vehicles (vehicle_id, vehicle_number, vehicle_type,  status, driver_id, driver_name, driver_contact, driver_nic, driver_email) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            LOGGER.info(vehicleDTO.toString());
            stmt.setObject(1, UUID.randomUUID());  // Generate unique vehicle ID
            stmt.setString(2, vehicleDTO.getVehicle_number());
            stmt.setString(3, vehicleDTO.getVehicle_type().toString());
            stmt.setString(4, "AVAILABLE");
            stmt.setObject(5, UUID.randomUUID());
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

    public boolean updateVehicle(VehicleDTO vehicleDTO) {
        if (vehicleDTO == null || vehicleDTO.getVehicle_id() == null) {
            return false;
        }

        VehicleDTO existingVehicle = getVehicleById(vehicleDTO.getVehicle_id());
        if (existingVehicle == null) {
            return false;
        }

        String query = "UPDATE vehicles SET vehicle_number = ?, vehicle_type = ?, status = ?, " +
                "driver_name = ?, driver_contact = ?, driver_nic = ?, driver_email = ? WHERE vehicle_id = ?";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            if (vehicleDTO.getVehicle_number() != null && !vehicleDTO.getVehicle_number().equals(existingVehicle.getVehicle_number())) {
                stmt.setString(1, vehicleDTO.getVehicle_number());
            } else {
                stmt.setString(1, existingVehicle.getVehicle_number());
            }

            if (vehicleDTO.getVehicle_type() != null && !vehicleDTO.getVehicle_type().equals(existingVehicle.getVehicle_type())) {
                stmt.setString(2, vehicleDTO.getVehicle_type().toString());
            } else {
                stmt.setString(2, existingVehicle.getVehicle_type().toString());
            }

            if (vehicleDTO.getStatus() != null && !vehicleDTO.getStatus().equals(existingVehicle.getStatus())) {
                stmt.setString(3, vehicleDTO.getStatus().toString());
            } else {
                stmt.setString(3, existingVehicle.getStatus().toString());
            }

            if (vehicleDTO.getDriver_name() != null && !vehicleDTO.getDriver_name().equals(existingVehicle.getDriver_name())) {
                stmt.setString(4, vehicleDTO.getDriver_name());
            } else {
                stmt.setString(4, existingVehicle.getDriver_name());
            }

            if (vehicleDTO.getDriver_contact() != null && !vehicleDTO.getDriver_contact().equals(existingVehicle.getDriver_contact())) {
                stmt.setString(5, vehicleDTO.getDriver_contact());
            } else {
                stmt.setString(5, existingVehicle.getDriver_contact());
            }

            if (vehicleDTO.getDriver_nic() != null && !vehicleDTO.getDriver_nic().equals(existingVehicle.getDriver_nic())) {
                stmt.setString(6, vehicleDTO.getDriver_nic());
            } else {
                stmt.setString(6, existingVehicle.getDriver_nic());
            }

            if (vehicleDTO.getDriver_email() != null && !vehicleDTO.getDriver_email().equals(existingVehicle.getDriver_email())) {
                stmt.setString(7, vehicleDTO.getDriver_email());
            } else {
                stmt.setString(7, existingVehicle.getDriver_email());
            }

            stmt.setObject(8, vehicleDTO.getVehicle_id());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            return false;
        }
    }

    private VehicleDTO getVehicleById(UUID vehicleId) {
        String query = "SELECT * FROM vehicles WHERE vehicle_id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setObject(1, vehicleId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                VehicleDTO vehicle = new VehicleDTO();
                vehicle.setVehicle_id((UUID) rs.getObject("vehicle_id"));
                vehicle.setVehicle_number(rs.getString("vehicle_number"));
                vehicle.setVehicle_type(VehicleTypes.valueOf(rs.getString("vehicle_type")));
                vehicle.setStatus(VehicleStatus.valueOf(rs.getString("status")));
                vehicle.setDriver_name(rs.getString("driver_name"));
                vehicle.setDriver_contact(rs.getString("driver_contact"));
                vehicle.setDriver_nic(rs.getString("driver_nic"));
                vehicle.setDriver_email(rs.getString("driver_email"));
                return vehicle;
            }
        } catch (SQLException e) {
            return null;
        }
        return null;
    }

    @Override
    public boolean deleteVehicle(UUID vehicleId) {
        LOGGER.info("Deleting vehicle with ID: {}", vehicleId);

        String query = "DELETE FROM vehicles WHERE vehicle_id = ?";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setObject(1, vehicleId);

            int rowsDeleted = stmt.executeUpdate();
            LOGGER.info("Vehicle deleted successfully");
            return rowsDeleted > 0;

        } catch (SQLException e) {
            LOGGER.error("Error deleting vehicle", e);
            throw new RuntimeException("Failed to delete vehicle", e);
        }
    }

    @Override
    public Vehicle[] getVehicles() {
        String query = "SELECT * FROM vehicles";
        try (Connection connection = DBConnection.getInstance().getConnection()) {
            LOGGER.info("Connected to database");
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    Vehicle[] vehicles = new Vehicle[resultSet.getMetaData().getColumnCount()];
                    LOGGER.info("Number of rows affected: " + resultSet.getMetaData().getColumnCount());
                    while (resultSet.next()) {
                        Vehicle vehicle = new Vehicle();


                        vehicle.setVehicle_id(UUID.fromString(resultSet.getString("vehicle_id")));
                        vehicle.setVehicle_number(resultSet.getString("vehicle_number"));
                        vehicle.setVehicle_type(resultSet.getString("vehicle_type"));
                        vehicle.setStatus(VehicleStatus.valueOf(resultSet.getString("status")));
                        vehicle.setDriver_id(UUID.fromString(resultSet.getString("driver_id")));
                        vehicle.setDriver_name(resultSet.getString("driver_name"));
                        vehicle.setDriver_contact(resultSet.getString("driver_contact"));
                        vehicle.setDriver_nic(resultSet.getString("driver_nic"));
                        vehicle.setDriver_email(resultSet.getString("driver_email"));
                        vehicles[resultSet.getRow() - 1] = vehicle;
                    }
                    LOGGER.info(Arrays.toString(vehicles));
                    return vehicles;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
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
        return null;
    }


}

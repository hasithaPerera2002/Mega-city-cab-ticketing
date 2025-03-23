package org.icbt.hasitha.megacity.service;

import jakarta.servlet.http.HttpServletResponse;
import org.icbt.hasitha.megacity.dto.ResultDTO;
import org.icbt.hasitha.megacity.dto.VehicleDTO;
import org.icbt.hasitha.megacity.entity.Vehicle;
import org.icbt.hasitha.megacity.repository.VehicleRepo;
import org.icbt.hasitha.megacity.service.interfaces.IVehicleService;
import org.icbt.hasitha.megacity.util.enums.VehicleStatus;
import org.icbt.hasitha.megacity.util.enums.VehicleTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class VehicleService implements IVehicleService {
    private final VehicleRepo vehicleRepository = new VehicleRepo();
    private final Logger LOGGER = LoggerFactory.getLogger(VehicleService.class);

    @Override
    public ResultDTO<Boolean> addVehicle(VehicleDTO vehicle) {

        try {
            boolean flag = vehicleRepository.addVehicle(vehicle);
            return new ResultDTO<Boolean>("Vehicle added successfully", HttpServletResponse.SC_CREATED, flag);

        } catch (Exception e) {
            LOGGER.error("Error adding vehicle: {}", e.getMessage());
            return new ResultDTO<Boolean>(e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR, false);
        }
    }

    @Override
    public VehicleDTO[] getVehicles() {
        try {
            LOGGER.info("Getting vehicles...");
            Vehicle[] vehicles = vehicleRepository.getVehicles();

            List<VehicleDTO> vehicleDTOs = Arrays.stream(vehicles)
                    .filter(Objects::nonNull)
                    .map(vehicle ->
                            new VehicleDTO(
                                    vehicle.getVehicle_id(),
                                    vehicle.getVehicle_number(),
                                    VehicleTypes.valueOf(vehicle.getVehicle_type()),
                                    VehicleStatus.valueOf(vehicle.getStatus().name()),
                                    vehicle.getDriver_id(), vehicle.getDriver_name(),
                                    vehicle.getDriver_contact(), vehicle.getDriver_nic(),
                                    vehicle.getDriver_email())).toList();
            return vehicleDTOs.toArray(new VehicleDTO[0]);

        } catch (Exception e) {
            LOGGER.error("Error getting vehicles: {}", e.getMessage());
        }
        return new VehicleDTO[0];
    }

    @Override
    public ResultDTO<Boolean> updateVehicle(VehicleDTO vehicleDTO) {
        LOGGER.info("Updating vehicle: {}", vehicleDTO);
        try {
            boolean isUpdated = vehicleRepository.updateVehicle(vehicleDTO);
            if (isUpdated) {
                return new ResultDTO<>("Vehicle updated successfully", HttpServletResponse.SC_OK, true);
            } else {
                return new ResultDTO<>("Failed to update vehicle", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, false);
            }
        } catch (Exception e) {
            LOGGER.error("Error updating vehicle: {}", e.getMessage());
            return new ResultDTO<>(e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR, false);
        }
    }

    @Override
    public ResultDTO<Boolean> deleteVehicle(UUID vehicleId) {
        LOGGER.info("Deleting vehicle with ID: {}", vehicleId);
        try {
            boolean isDeleted = vehicleRepository.deleteVehicle(vehicleId);
            if (isDeleted) {
                return new ResultDTO<>("Vehicle deleted successfully", HttpServletResponse.SC_OK, true);
            } else {
                return new ResultDTO<>("Failed to delete vehicle", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, false);
            }
        } catch (Exception e) {
            LOGGER.error("Error deleting vehicle: {}", e.getMessage());
            return new ResultDTO<>(e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR, false);
        }
    }

}

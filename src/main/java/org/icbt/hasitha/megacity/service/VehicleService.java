package org.icbt.hasitha.megacity.service;

import jakarta.servlet.http.HttpServletResponse;
import org.icbt.hasitha.megacity.dto.ResultDTO;
import org.icbt.hasitha.megacity.dto.VehicleDTO;
import org.icbt.hasitha.megacity.entity.Vehicle;
import org.icbt.hasitha.megacity.repository.VehicleRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VehicleService {
    private final VehicleRepo vehicleRepository = new VehicleRepo();
    private final Logger LOGGER = LoggerFactory.getLogger(VehicleService.class);

    public ResultDTO<Boolean> addVehicle(VehicleDTO vehicle) {

        try {
            boolean flag = vehicleRepository.addVehicle(vehicle);
            return new ResultDTO<Boolean>("Vehicle added successfully", HttpServletResponse.SC_CREATED, flag);
        } catch (Exception e) {
            LOGGER.error("Error adding vehicle: {}", e.getMessage());
            return new ResultDTO<Boolean>(e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR, false);
        }
    }
}

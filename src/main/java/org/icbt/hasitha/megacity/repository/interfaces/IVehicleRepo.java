package org.icbt.hasitha.megacity.repository.interfaces;

import org.icbt.hasitha.megacity.dto.VehicleDTO;
import org.icbt.hasitha.megacity.entity.Vehicle;
import org.icbt.hasitha.megacity.util.enums.VehicleTypes;

import java.util.UUID;

public interface IVehicleRepo extends ISuperRepo{
    boolean addVehicle(VehicleDTO vehicleDTO);
    boolean updateVehicle(VehicleDTO vehicleDTO);
    boolean deleteVehicle(UUID vehicleId);
    Vehicle[] getVehicles();
    UUID getVehicleIdByType(VehicleTypes vehicleType);
}

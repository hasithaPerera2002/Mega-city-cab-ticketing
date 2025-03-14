package org.icbt.hasitha.megacity.service.interfaces;

import org.icbt.hasitha.megacity.dto.ResultDTO;
import org.icbt.hasitha.megacity.dto.VehicleDTO;

import java.util.UUID;

public interface IVehicleService extends ISuperService{
    ResultDTO<Boolean> addVehicle(VehicleDTO vehicle);
    ResultDTO<Boolean> updateVehicle(VehicleDTO vehicleDTO);
    VehicleDTO[] getVehicles();
    ResultDTO<Boolean> deleteVehicle(UUID vehicleId);
}

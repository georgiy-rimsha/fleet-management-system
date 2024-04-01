package dev.notenger.vehicle.service;

import dev.notenger.vehicle.entity.Vehicle;
import dev.notenger.vehicle.web.VehicleDTO;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class VehicleDTOMapper implements Function<Vehicle, VehicleDTO> {
    @Override
    public VehicleDTO apply(Vehicle vehicle) {

        return new VehicleDTO(
                vehicle.getId(),
                vehicle.getVinNumber(),
                vehicle.getDeviceId(),
                vehicle.getMake(),
                vehicle.getModel(),
                vehicle.getYear()
        );
    }
}

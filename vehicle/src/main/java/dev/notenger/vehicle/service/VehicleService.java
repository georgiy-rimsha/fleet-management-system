package dev.notenger.vehicle.service;

import dev.notenger.clients.device.DeviceClient;
import dev.notenger.clients.device.ReserveDeviceRequest;
import dev.notenger.clients.device.ReserveDeviceResponse;
import dev.notenger.vehicle.entity.Vehicle;
import dev.notenger.clients.vehicle.exception.DuplicateVehicleException;
import dev.notenger.clients.vehicle.exception.InvalidVinException;
import dev.notenger.clients.vehicle.exception.NoChangesDetectedException;
import dev.notenger.clients.vehicle.exception.VehicleNotFoundException;
import dev.notenger.vehicle.repository.VehicleDao;
import dev.notenger.vehicle.web.VehicleDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class VehicleService {

    private final VehicleDao vehicleDao;
    private final VehicleDTOMapper vehicleDTOMapper;
    private final DeviceClient deviceClient;

    public Vehicle addVehicle(String vin, String make, String model, Integer year) {
        // check if vin is neither null nor empty
        if (vin == null || vin.isEmpty()) {
            throw new InvalidVinException("vin is required");
        }

        // check if vin exists
        if (vehicleDao.existsVehicleByVin(vin)) {
            throw new DuplicateVehicleException(
                    "vin already taken"
            );
        }

        Vehicle vehicle = Vehicle
                .builder()
                .vin(vin)
                .make(make)
                .model(model)
                .year(year)
                .build();
        vehicleDao.insertVehicle(vehicle);

        return vehicle;
    }

    public void bindToDevice(Integer vehicleId) {
        Vehicle vehicle = vehicleDao.selectVehicleById(vehicleId)
                .orElseThrow(() -> new VehicleNotFoundException(
                        "vehicle with id [%s] not found".formatted(vehicleId)
                ));
        ReserveDeviceRequest request = new ReserveDeviceRequest(vehicle.getId());
        ReserveDeviceResponse response = deviceClient.reserveDevice(request);
        vehicle.setDeviceId(response.deviceId());
        vehicleDao.updateVehicle(vehicle);
    }

    public VehicleDTO getVehicle(Integer id) {
        return vehicleDao.selectVehicleById(id)
                .map(vehicleDTOMapper)
                .orElseThrow(() -> new VehicleNotFoundException(
                        "vehicle with id [%s] not found".formatted(id)
                ));
    }

    public void updateVehicle(Integer id, String vin, String make, String model, Integer year) {
        Vehicle vehicle = vehicleDao.selectVehicleById(id)
                .orElseThrow(() -> new VehicleNotFoundException(
                        "vehicle with id [%s] not found".formatted(id)
                ));

        boolean changes = false;

        if (vin != null && !vin.equals(vehicle.getVin())) {
            if (vehicleDao.existsVehicleByVin(vin)) {
                throw new DuplicateVehicleException(
                        "vin already taken"
                );
            }
            vehicle.setVin(vin);
            changes = true;
        }

        if (make != null && !make.equals(vehicle.getMake())) {
            vehicle.setMake(make);
            changes = true;
        }

        if (model != null && !model.equals(vehicle.getModel())) {
            vehicle.setModel(model);
            changes = true;
        }

        if (year != null && !year.equals(vehicle.getYear())) {
            vehicle.setYear(year);
            changes = true;
        }

        if (!changes) {
            throw new NoChangesDetectedException("no data changes found");
        }

        vehicleDao.updateVehicle(vehicle);
    }

    public void deleteVehicle(Integer vehicleId) {
        checkIfVehicleExistsOrThrow(vehicleId);
        vehicleDao.deleteVehicleById(vehicleId);
    }

    private void checkIfVehicleExistsOrThrow(Integer vehicleId) {
        if (!vehicleDao.existsVehicleById(vehicleId)) {
            throw new VehicleNotFoundException(
                    "vehicle with id [%s] not found".formatted(vehicleId)
            );
        }
    }

    public List<VehicleDTO> getAllVehicles() {
        return vehicleDao.selectAllVehicles()
                .stream()
                .map(vehicleDTOMapper)
                .collect(Collectors.toList());
    }
}

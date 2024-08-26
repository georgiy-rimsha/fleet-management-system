package dev.notenger.vehicle.service;

import dev.notenger.clients.device.AttachDeviceRequest;
import dev.notenger.clients.device.DeviceClient;
import dev.notenger.clients.telematics.GetOdometerResponse;
import dev.notenger.clients.telematics.TelematicsClient;
import dev.notenger.clients.vehicle.exception.DuplicateVehicleException;
import dev.notenger.clients.vehicle.exception.InvalidVinException;
import dev.notenger.clients.vehicle.exception.NoChangesDetectedException;
import dev.notenger.clients.vehicle.exception.VehicleNotFoundException;
import dev.notenger.vehicle.entity.Vehicle;
import dev.notenger.vehicle.repository.VehicleDao;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class VehicleService {

    private final VehicleDao vehicleDao;
    private final DeviceClient deviceClient;
    private final TelematicsClient telematicsClient;

    public void addVehicle(
            String vin,
            String make,
            String model,
            Integer year,
            String groupName,
            Double averageSpeed,
            Integer deviceId) {
        // check if vin is neither null nor empty
        if (vin == null || vin.isEmpty()) {
            throw new InvalidVinException("vin is required");
        }

        // check if vin exists
        if (vehicleDao.existsVehicleByVin(vin)) {
            throw new DuplicateVehicleException(
                    "vin already present"
            );
        }

        Vehicle vehicle = Vehicle
                .builder()
                .vin(vin)
                .make(make)
                .model(model)
                .year(year)
                .groupName(groupName)
                .averageSpeed(averageSpeed)
                .deviceId(deviceId)
                .build();
        vehicleDao.insertVehicle(vehicle);

        AttachDeviceRequest request = new AttachDeviceRequest(groupName, averageSpeed);
        deviceClient.attachDevice(deviceId, request);
    }

    public void deleteVehicle(Integer vehicleId) {
        Vehicle vehicle = vehicleDao.selectVehicleById(vehicleId)
                .orElseThrow(() -> new VehicleNotFoundException(
                        "vehicle with id [%s] not found".formatted(vehicleId)
                ));
        deviceClient.detachDevice(vehicle.getDeviceId());
        vehicleDao.deleteVehicleById(vehicleId);
    }

    public Vehicle getVehicle(Integer vehicleId) {
        return vehicleDao.selectVehicleById(vehicleId)
                .orElseThrow(() -> new VehicleNotFoundException(
                        "vehicle with id [%s] not found".formatted(vehicleId)
                ));
    }

    public void updateOdometer(Integer vehicleId) {
        Vehicle vehicle = vehicleDao.selectVehicleById(vehicleId)
                .orElseThrow(() -> new VehicleNotFoundException(
                        "vehicle with id [%s] not found".formatted(vehicleId)
                ));

        GetOdometerResponse response = telematicsClient.getLastOdometerReading(vehicle.getDeviceId());
        vehicle.setLastOdometerReading(response.odometer());
        vehicleDao.updateVehicle(vehicle);
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
                        "vin already present"
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

    public List<Vehicle> getAllVehicles() {
        return vehicleDao.selectAllVehicles();
    }
}

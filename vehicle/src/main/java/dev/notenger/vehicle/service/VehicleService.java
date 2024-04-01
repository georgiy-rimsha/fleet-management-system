package dev.notenger.vehicle.service;

import dev.notenger.clients.device.DeviceClient;
import dev.notenger.clients.device.ReserveDeviceRequest;
import dev.notenger.clients.device.ReserveDeviceResponse;
import dev.notenger.clients.telematics.TelematicsClient;
import dev.notenger.vehicle.entity.Vehicle;
import dev.notenger.vehicle.exception.ResourceNotFoundException;
import dev.notenger.vehicle.repository.VehicleDao;
import dev.notenger.vehicle.web.VehicleDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class VehicleService {

    private final VehicleDao vehicleDao;
    private final VehicleDTOMapper vehicleDTOMapper;
    private final DeviceClient deviceClient;

    public Vehicle addVehicle(String vinNumber, String make, String model, Integer year) {
        // check if model exists
        if (vehicleDao.existsVehicleByModel(model)) {
//            throw new IllegalArgumentException(
//                    "model already taken"
//            );
        }

        Vehicle vehicle = Vehicle
                .builder()
                .vinNumber(vinNumber)
                .make(make)
                .model(model)
                .year(year)
                .build();
        vehicleDao.insertVehicle(vehicle);

        return vehicle;
    }

    public void bindToDevice(Integer id) {
        Vehicle vehicle = vehicleDao.selectVehicleById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "vehicle with id [%s] not found".formatted(id)
                ));
        ReserveDeviceRequest request = new ReserveDeviceRequest(vehicle.getId());
        ReserveDeviceResponse response = deviceClient.reserveDevice(request);
        vehicle.setDeviceId(response.deviceId());
        vehicleDao.updateVehicle(vehicle);
    }

    public VehicleDTO getVehicle(Integer id) {
        return vehicleDao.selectVehicleById(id)
                .map(vehicleDTOMapper)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "vehicle with id [%s] not found".formatted(id)
                ));
    }

    public void updateVehicle(Integer id, String make, String model, Integer year) {
        Vehicle vehicle = vehicleDao.selectVehicleById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "vehicle with id [%s] not found".formatted(id)
                ));

        boolean changes = false;

        if (make != null && !make.equals(vehicle.getMake())) {
            vehicle.setMake(make);
            changes = true;
        }

        if (year != null && !year.equals(vehicle.getYear())) {
            vehicle.setYear(year);
            changes = true;
        }

        if (model != null && !model.equals(vehicle.getModel())) {
            if (vehicleDao.existsVehicleByModel(model)) {
                throw new IllegalArgumentException(
                        "model already taken"
                );
            }
            vehicle.setModel(model);
            changes = true;
        }

        if (!changes) {
            throw new IllegalArgumentException("no data changes found");
        }

        vehicleDao.updateVehicle(vehicle);
    }

    public void deleteVehicle(Integer id) {
        checkIfVehicleExistsOrThrow(id);
        vehicleDao.deleteVehicleById(id);
    }

    private void checkIfVehicleExistsOrThrow(Integer id) {
        if (!vehicleDao.existsVehicleById(id)) {
            throw new IllegalArgumentException(
                    "vehicle with id [%s] not found".formatted(id)
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

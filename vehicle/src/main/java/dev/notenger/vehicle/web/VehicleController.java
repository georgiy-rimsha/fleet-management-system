package dev.notenger.vehicle.web;

import dev.notenger.clients.vehicle.AddVehicleRequest;
import dev.notenger.clients.vehicle.UpdateVehicleRequest;
import dev.notenger.vehicle.entity.Vehicle;
import dev.notenger.vehicle.service.VehicleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "api/v1/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping
    public void addVehicle(@RequestBody AddVehicleRequest request) {
        log.info("Add vehicle request. VIN is {}", request.vin());
        vehicleService.addVehicle(
                request.vin(),
                request.make(),
                request.model(),
                request.year(),
                request.groupName(),
                request.averageSpeed(),
                request.deviceId());
    }

    @GetMapping
    public List<Vehicle> getVehicles() {
        log.info("Requested all vehicles");
        return vehicleService.getAllVehicles();
    }

    @GetMapping("{vehicleId}")
    public Vehicle getVehicle(@PathVariable Integer vehicleId) {
        log.info("Requested vehicle with id {}", vehicleId);
        return vehicleService.getVehicle(vehicleId);
    }

    @PutMapping("{vehicleId}")
    public void updateVehicle(
            @PathVariable("vehicleId") Integer vehicleId,
            @RequestBody UpdateVehicleRequest updateRequest) {
        log.info("Update vehicle request. Vehicle's id is {}", vehicleId);
        vehicleService.updateVehicle(
                vehicleId, updateRequest.vin(), updateRequest.make(), updateRequest.model(), updateRequest.year());
    }

    @PutMapping("update-odometer/{vehicleId}")
    public void updateOdometer(@PathVariable("vehicleId") Integer vehicleId) {
        log.info("Refresh odometer request for vehicle with id {}", vehicleId);
        vehicleService.updateOdometer(vehicleId);
    }

    @DeleteMapping("{vehicleId}")
    public void deleteVehicle(@PathVariable("vehicleId") Integer vehicleId) {
        log.info("Delete vehicle request. Vehicle's id is {}", vehicleId);
        vehicleService.deleteVehicle(vehicleId);
    }

}

package dev.notenger.vehicle.web;

import dev.notenger.clients.vehicle.AddVehicleRequest;
import dev.notenger.clients.vehicle.AddVehicleResponse;
import dev.notenger.clients.vehicle.UpdateVehicleRequest;
import dev.notenger.vehicle.entity.Vehicle;
import dev.notenger.vehicle.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(path = "api/v1/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping
    public AddVehicleResponse addVehicle(@RequestBody AddVehicleRequest request) {
        Vehicle vehicle = vehicleService.addVehicle(request.vinNumber(), request.make(), request.model(), request.year());
        return new AddVehicleResponse(vehicle.getId());
    }

    @PutMapping("bind/{vehicleId}")
    public void bindToDevice(@PathVariable("vehicleId") Integer vehicleId) {
        vehicleService.bindToDevice(vehicleId);
    }

    @GetMapping
    public List<VehicleDTO> getVehicles() {
        return vehicleService.getAllVehicles();
    }

    @GetMapping("{vehicleId}")
    public VehicleDTO getVehicle(@PathVariable Integer vehicleId) {
        return vehicleService.getVehicle(vehicleId);
    }

    @PutMapping("{vehicleId}")
    public void updateVehicle(
            @PathVariable("vehicleId") Integer vehicleId,
            @RequestBody UpdateVehicleRequest updateRequest) {
        vehicleService.updateVehicle(vehicleId, updateRequest.make(), updateRequest.model(), updateRequest.year());
    }

    @DeleteMapping("{vehicleId}")
    public void deleteVehicle(@PathVariable("vehicleId") int vehicleId) {
        vehicleService.deleteVehicle(vehicleId);
    }

}

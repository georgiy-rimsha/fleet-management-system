package dev.notenger.vehicle.init;

import dev.notenger.clients.vehicle.AddVehicleRequest;
import dev.notenger.vehicle.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitData implements CommandLineRunner {

    private final VehicleService vehicleService;

    @Override
    public void run(String... args) throws Exception {
        AddVehicleRequest request;

//        request = new CreateVehicleRequest(1, new double[]{50, 50}, 100);
//        vehicleService.addVehicle(request);
//
//        request = new CreateVehicleRequest(2,1);
//        vehicleService.addVehicle(request);
//
//        request = new CreateVehicleRequest(3,2);
//        vehicleService.addVehicle(request);
//
//        request = new CreateVehicleRequest(4,3);
//        vehicleService.addVehicle(request);
    }
}

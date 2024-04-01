package dev.notenger.vehicle.repository;

import dev.notenger.vehicle.entity.Vehicle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class VehicleJPADataAccessService implements VehicleDao {

    private final VehicleRepository vehicleRepository;
    @Override
    public List<Vehicle> selectAllVehicles() {
        return vehicleRepository.findAll();
    }

    @Override
    public Optional<Vehicle> selectVehicleById(Integer vehicleId) {
        return vehicleRepository.findById(vehicleId);
    }

    @Override
    public void insertVehicle(Vehicle vehicle) {
        vehicleRepository.save(vehicle);
    }

    @Override
    public boolean existsVehicleById(Integer vehicleId) {
        return vehicleRepository.existsVehicleById(vehicleId);
    }

    @Override
    public boolean existsVehicleByModel(String model) {
        return vehicleRepository.existsVehicleByModel(model);
    }

    @Override
    public void deleteVehicleById(Integer vehicleId) {
        vehicleRepository.deleteById(vehicleId);
    }

    @Override
    public void updateVehicle(Vehicle update) {
        vehicleRepository.save(update);
    }
}

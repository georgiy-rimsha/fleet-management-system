package dev.notenger.vehicle.repository;

import dev.notenger.vehicle.entity.Vehicle;

import java.util.List;
import java.util.Optional;

public interface VehicleDao {
    List<Vehicle> selectAllVehicles();
    Optional<Vehicle> selectVehicleById(Integer VehicleId);
    void insertVehicle(Vehicle Vehicle);
    boolean existsVehicleById(Integer VehicleId);
    boolean existsVehicleByModel(String model);
    void deleteVehicleById(Integer VehicleId);
    void updateVehicle(Vehicle update);
}

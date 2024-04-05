package dev.notenger.vehicle.repository;

import dev.notenger.vehicle.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {
    boolean existsVehicleById(Integer id);
    boolean existsVehicleByVin(String model);

}

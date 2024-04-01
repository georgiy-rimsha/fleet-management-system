package dev.notenger.vehicle.repository;

import dev.notenger.vehicle.entity.Vehicle;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class VehicleListRepository {

    private List<Vehicle> vehicleList = new ArrayList<>();

    public void save(Vehicle vehicle) {
        vehicleList.add(vehicle);
    }

    public Optional<Vehicle> findById(int vehicleId) {
        return vehicleList.stream().filter(v -> v.getId() == vehicleId).findAny();
    }

    public void deleteById(int vehicleId) {
        vehicleList.remove(vehicleList.stream().filter(v -> v.getId() == vehicleId).findAny().get());
    }

    public List<Vehicle> findAll() {
        return vehicleList;
    }
}

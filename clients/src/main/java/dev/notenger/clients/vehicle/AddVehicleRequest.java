package dev.notenger.clients.vehicle;

public record AddVehicleRequest(
        String vinNumber,
        String make,
        String model,
        Integer year) {
}

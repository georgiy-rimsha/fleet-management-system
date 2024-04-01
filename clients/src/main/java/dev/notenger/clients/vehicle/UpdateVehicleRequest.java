package dev.notenger.clients.vehicle;

public record UpdateVehicleRequest(
        String make,
        String model,
        Integer year) {
}

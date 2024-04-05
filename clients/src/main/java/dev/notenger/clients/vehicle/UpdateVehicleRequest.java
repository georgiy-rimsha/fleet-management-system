package dev.notenger.clients.vehicle;

public record UpdateVehicleRequest(
        String vin,
        String make,
        String model,
        Integer year) {
}

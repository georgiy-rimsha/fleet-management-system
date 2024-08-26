package dev.notenger.clients.vehicle;

public record AddVehicleRequest(
        String vin,
        String make,
        String model,
        Integer year,
        String groupName,
        Double averageSpeed,
        Integer deviceId) {
}

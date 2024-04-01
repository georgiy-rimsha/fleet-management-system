package dev.notenger.clients.device;

public record DeviceDTO(
        Integer id,
        Integer vehicleId,
        String deviceSerialNumber,
        double averageSpeed,
        Boolean available
) {
}

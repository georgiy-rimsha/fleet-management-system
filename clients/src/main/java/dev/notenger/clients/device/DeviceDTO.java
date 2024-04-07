package dev.notenger.clients.device;

public record DeviceDTO(
        Integer id,
        Integer vehicleId,
        String serialNumber,
        double averageSpeed,
        Boolean available
) {
}

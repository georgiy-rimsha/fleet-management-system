package dev.notenger.clients.device;

public record DeviceDTO(
        Integer id,
        String serialNumber,
        Double averageSpeed,
        Boolean available) {
}

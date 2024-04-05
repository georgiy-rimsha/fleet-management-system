package dev.notenger.clients.device;

public record RegisterDeviceRequest(
        String serialNumber,
        String placeName,
        Double averageSpeed) {
}

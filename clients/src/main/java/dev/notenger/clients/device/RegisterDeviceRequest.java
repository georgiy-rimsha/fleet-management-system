package dev.notenger.clients.device;

public record RegisterDeviceRequest(
        String deviceSerialNumber,
        String placeName,
        double averageSpeed) {
}

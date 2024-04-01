package dev.notenger.telematics.messaging;

import java.util.List;

public record GeolocationDTO(
        Integer deviceId,
        Integer vehicleId,
        double latitude,
        double longitude,
        double heading,
        List<List<Double>> pathData) {
}

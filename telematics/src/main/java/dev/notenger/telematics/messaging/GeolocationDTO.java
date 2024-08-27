package dev.notenger.telematics.messaging;

import java.util.List;

public record GeolocationDTO(
        Integer deviceId,
        Double latitude,
        Double longitude,
        Double heading,
        List<List<Double>> pathData) {
}

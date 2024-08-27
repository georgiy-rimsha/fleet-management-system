package dev.notenger.telematics.messaging;


public record TelemetryDTO(
        Double averageSpeed,
        Double averageOdometer,
        Double averageFuelGauge) {
}
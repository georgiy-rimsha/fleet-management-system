package dev.notenger.vehicle.web;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;

public record VehicleDTO(
        Integer id,
        String vin,
        String make,
        String model,
        Integer year,
        Integer deviceId) {
}
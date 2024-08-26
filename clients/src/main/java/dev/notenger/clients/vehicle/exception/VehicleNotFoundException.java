package dev.notenger.clients.vehicle.exception;

import dev.notenger.clients.config.ResourceNotFoundException;

public class VehicleNotFoundException extends ResourceNotFoundException {

    public VehicleNotFoundException(String message) {
        super(message);
    }
}

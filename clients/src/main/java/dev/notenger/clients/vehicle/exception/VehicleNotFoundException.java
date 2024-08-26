package dev.notenger.clients.vehicle.exception;

public class VehicleNotFoundException extends ResourceNotFoundException {

    public VehicleNotFoundException(String message) {
        super(message);
    }
}

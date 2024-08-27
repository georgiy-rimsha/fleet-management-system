package dev.notenger.clients.telematics.exception;

import dev.notenger.clients.config.ResourceNotFoundException;

public class TelemetryNotFoundException extends ResourceNotFoundException {
    public TelemetryNotFoundException(String message) {
        super(message);
    }
}

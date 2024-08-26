package dev.notenger.clients.device.exception;

import dev.notenger.clients.config.ResourceNotFoundException;

public class DeviceNotFoundException extends ResourceNotFoundException {
    public DeviceNotFoundException(String message) {
        super(message);
    }
}

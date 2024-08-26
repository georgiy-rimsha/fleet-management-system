package dev.notenger.clients.place.exception;

import dev.notenger.clients.config.ResourceNotFoundException;

public class PlaceNotFoundException extends ResourceNotFoundException {
    public PlaceNotFoundException(String message) {
        super(message);
    }
}

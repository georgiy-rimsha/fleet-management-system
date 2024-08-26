package dev.notenger.clients.vehicle.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class NoChangesDetectedException extends RuntimeException {
    public NoChangesDetectedException(String message) {
        super(message);
    }
}

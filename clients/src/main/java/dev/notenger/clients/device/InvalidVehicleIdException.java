package dev.notenger.clients.device;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class InvalidVehicleIdException extends RuntimeException {
    public InvalidVehicleIdException(String message) {
        super(message);
    }
}

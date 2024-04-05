package dev.notenger.clients.device;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NoDeviceAvailableException extends RuntimeException {
    public NoDeviceAvailableException(String message) {
        super(message);
    }
}

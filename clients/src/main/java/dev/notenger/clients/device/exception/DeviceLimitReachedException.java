package dev.notenger.clients.device.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class DeviceLimitReachedException extends RuntimeException {
    public DeviceLimitReachedException(String message) {
        super(message);
    }
}

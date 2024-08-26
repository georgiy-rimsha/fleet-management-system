package dev.notenger.clients.place.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class DuplicatePlaceException extends RuntimeException {
    public DuplicatePlaceException(String message) {
        super(message);
    }
}

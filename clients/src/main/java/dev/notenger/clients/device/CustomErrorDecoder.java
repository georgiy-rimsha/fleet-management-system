package dev.notenger.clients.device;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;

public class CustomErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder errorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        return switch (response.status()) {
            case 400 -> new InvalidVehicleIdException("vehicle id is required");
            case 404 -> new NoDeviceAvailableException("no available devices found");
            default -> errorDecoder.decode(methodKey, response);
        };
    }
}

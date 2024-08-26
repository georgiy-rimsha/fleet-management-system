package dev.notenger.clients.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.notenger.clients.device.exception.DeviceNotFoundException;
import dev.notenger.clients.place.exception.PlaceNotFoundException;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;

import java.io.IOException;

public class CustomErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder errorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {

        return switch (response.status()) {
            case 404 -> {
                String jsonResponse = extractErrorMessage(response);
                ObjectMapper objectMapper = new ObjectMapper();
                ErrorResponse errorResponse;
                try {
                    errorResponse = objectMapper.readValue(jsonResponse, ErrorResponse.class);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }

                String errorMessage = errorResponse.getMessage();
                if (errorMessage.contains("place"))
                    yield new PlaceNotFoundException(errorMessage);
                if (errorMessage.contains("device"))
                    yield new DeviceNotFoundException(errorMessage);
                yield new ResourceNotFoundException(errorMessage);
            }
            default -> errorDecoder.decode(methodKey, response);
        };
    }

    private String extractErrorMessage(Response response) {
        try {
            return Util.toString(response.body().asReader());
        } catch (IOException e) {
            return "Could not extract error message";
        }
    }
}

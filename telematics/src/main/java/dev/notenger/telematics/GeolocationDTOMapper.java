package dev.notenger.telematics;

import dev.notenger.telematics.messaging.GeolocationDTO;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class GeolocationDTOMapper implements Function<Telemetry, GeolocationDTO> {
    @Override
    public GeolocationDTO apply(Telemetry telemetry) {
        return new GeolocationDTO(
                telemetry.getDeviceId(),
                telemetry.getLatitude(),
                telemetry.getLongitude(),
                telemetry.getHeading(),
                telemetry.getPathData());
    }
}

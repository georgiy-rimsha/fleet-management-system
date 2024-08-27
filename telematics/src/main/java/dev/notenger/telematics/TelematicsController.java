package dev.notenger.telematics;

import dev.notenger.clients.telematics.GetOdometerResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping(path = "api/v1/telemetry")
public class TelematicsController {

    private final TelematicsService telematicsService;

    @GetMapping("last-odometer-reading/{deviceId}")
    public GetOdometerResponse getLastOdometerReading(@PathVariable("deviceId") Integer deviceId) {
        log.info("Requested last odometer reading for device with id {}", deviceId);
        return new GetOdometerResponse(telematicsService.getLastOdometerReadingByDeviceId(deviceId));
    }
}

package dev.notenger.clients.telematics;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "${clients.telematics.name}",
        url = "${clients.telematics.url}",
        path = "api/v1/telemetry"
)
public interface TelematicsClient {

    @GetMapping("last-odometer-reading/{deviceId}")
    GetOdometerResponse getLastOdometerReading(@PathVariable("deviceId") Integer deviceId);

}

package dev.notenger.clients.device;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "device",
        url = "${clients.device.url}",
        configuration = CustomErrorDecoder.class
)
public interface DeviceClient {

    @GetMapping("api/v1/devices/{deviceId}")
    DeviceDTO getDevice(@PathVariable("deviceId") Integer deviceId);

    @PostMapping("api/v1/devices")
    RegisterDeviceResponse registerDevice(@RequestBody RegisterDeviceRequest request);

    @DeleteMapping("api/v1/devices/{deviceId}")
    void unregisterDevice(@PathVariable("deviceId") Integer deviceId);

    @PutMapping("api/v1/devices/{deviceId}")
    void updateDevice(@PathVariable("deviceId") Integer deviceId, @RequestBody UpdateDeviceRequest request);

    @PutMapping("api/v1/devices/reserve")
    ReserveDeviceResponse reserveDevice(@RequestBody ReserveDeviceRequest request);
}

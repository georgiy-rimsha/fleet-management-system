package dev.notenger.clients.device;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "device",
        url = "${clients.device.url}"
)
@RequestMapping("/api/v1/devices")
public interface DeviceClient {

    @GetMapping("{deviceId}")
    public DeviceDTO getDevice(@PathVariable("deviceId") Integer deviceId);

    @PostMapping
    RegisterDeviceResponse registerDevice(@RequestBody RegisterDeviceRequest request);

    @DeleteMapping("{deviceId}")
    void unregisterDevice(@PathVariable("deviceId") Integer deviceId);

    @PutMapping("{deviceId}")
    public void updateDevice(@PathVariable("deviceId") Integer deviceId, @RequestBody UpdateDeviceRequest request);

    @PutMapping("/reserve")
    ReserveDeviceResponse reserveDevice(@RequestBody ReserveDeviceRequest request);
}

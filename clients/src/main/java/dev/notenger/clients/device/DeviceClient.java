package dev.notenger.clients.device;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
        name = "${clients.device.name}",
        url = "${clients.device.url}",
        path = "api/v1/devices"
)
public interface DeviceClient {

    @PostMapping
    void registerDevice(@RequestBody RegisterDeviceRequest request);

    @GetMapping
    List<DeviceDTO> getDevices();

    @GetMapping("available")
    List<DeviceDTO> getAvailableDevices();

    @GetMapping("{deviceId}")
    DeviceDTO getDevice(@PathVariable("deviceId") Integer deviceId);

    @PutMapping("{deviceId}")
    void updateDevice(@PathVariable("deviceId") Integer deviceId, @RequestBody UpdateDeviceRequest request);

    @PutMapping("attach/{deviceId}")
    void attachDevice(@PathVariable("deviceId") Integer deviceId, @RequestBody AttachDeviceRequest request);

    @PutMapping("detach/{deviceId}")
    void detachDevice(@PathVariable("deviceId") Integer deviceId);

    @DeleteMapping("{deviceId}")
    void deregisterDevice(@PathVariable("deviceId") Integer deviceId);
}

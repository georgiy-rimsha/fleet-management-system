package dev.notenger.simulation.device;

import dev.notenger.clients.device.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("api/v1/devices")
@RequiredArgsConstructor
@CrossOrigin
@RestController
public class DeviceController {

    private final DeviceService deviceService;

    @GetMapping
    List<DeviceDTO> getDevices() {
        return deviceService.getAllDevices();
    }

    @GetMapping("{deviceId}")
    public DeviceDTO getDevice(@PathVariable("deviceId") Integer deviceId) {
        return deviceService.getDevice(deviceId);
    }

    @PostMapping
    RegisterDeviceResponse registerDevice(@RequestBody RegisterDeviceRequest request) {
        Device device = deviceService.registerDevice(request.placeName(), request.averageSpeed());
         return new RegisterDeviceResponse(device.getID());
    }

    @PutMapping("/reserve")
    ReserveDeviceResponse reserveDevice(@RequestBody ReserveDeviceRequest request) throws InterruptedException {
        Device device = deviceService.reserveAndGetAvailableDevice(request.vehicleId());
        return new ReserveDeviceResponse(device.getID());
    }

    @DeleteMapping("{deviceId}")
    void unregisterDevice(@PathVariable("deviceId") Integer deviceId) {
        deviceService.unregisterDevice(deviceId);
    }

    @PutMapping("{deviceId}")
    public void updateDevice(@PathVariable("deviceId") Integer deviceId, @RequestBody UpdateDeviceRequest request) {
        log.info("requested averageSpeed is " + request.averageSpeed());
        deviceService.updateDevice(deviceId, request.averageSpeed());
    }

}
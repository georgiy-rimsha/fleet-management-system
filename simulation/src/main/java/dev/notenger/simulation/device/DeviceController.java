package dev.notenger.simulation.device;

import dev.notenger.clients.device.AttachDeviceRequest;
import dev.notenger.clients.device.DeviceDTO;
import dev.notenger.clients.device.RegisterDeviceRequest;
import dev.notenger.clients.device.UpdateDeviceRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("api/v1/devices")
@RequiredArgsConstructor
@RestController
public class DeviceController {

    private final DeviceService deviceService;

    @PostMapping
    public void registerDevice(@RequestBody RegisterDeviceRequest request) {
        log.info("Register device request. Serial number is {}", request.serialNumber());
        deviceService.registerDevice(request.serialNumber());
    }

    @GetMapping
    public List<DeviceDTO> getDevices() {
        log.info("Requested all devices");
        return deviceService.getAllDevices();
    }

    @GetMapping("available")
    public List<DeviceDTO> getAvailableDevices() {
        log.info("Requested all available devices");
        return deviceService.getAllAvailableDevices();
    }

    @GetMapping("{deviceId}")
    public DeviceDTO getDevice(@PathVariable("deviceId") Integer deviceId) {
        log.info("Requested device with id {}", deviceId);
        return deviceService.getDevice(deviceId);
    }

    @PutMapping("{deviceId}")
    public void updateDevice(@PathVariable("deviceId") Integer deviceId, @RequestBody UpdateDeviceRequest request) {
        log.info("Update device request. Device's id is {}", deviceId);
        deviceService.updateDevice(deviceId, request.averageSpeed());
    }

    @PutMapping("attach/{deviceId}")
    public void attachDevice(@PathVariable("deviceId") Integer deviceId, @RequestBody AttachDeviceRequest request) {
        log.info("Attach device request. Device's id is {}", deviceId);
        deviceService.attachDevice(deviceId, request.placeName(), request.averageSpeed());
    }

    @PutMapping("detach/{deviceId}")
    public void detachDevice(@PathVariable("deviceId") Integer deviceId) {
        log.info("Detach device request. Device's id is {}", deviceId);
        deviceService.detachDevice(deviceId);
    }

    @DeleteMapping("{deviceId}")
    public void deregisterDevice(@PathVariable("deviceId") Integer deviceId) {
        log.info("Deregister device request. Device's id is {}", deviceId);
        deviceService.deregisterDevice(deviceId);
    }

}
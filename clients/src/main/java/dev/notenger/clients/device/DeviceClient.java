package dev.notenger.clients.device;

import dev.notenger.clients.LoadBalancerConfiguration;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "simulation",
//        url = "${clients.device.url}",
        path = "api/v1/devices",
        configuration = CustomErrorDecoder.class
)
@LoadBalancerClient(name = "simulation", configuration = LoadBalancerConfiguration.class)
public interface DeviceClient {

    @GetMapping("{deviceId}")
    DeviceDTO getDevice(@PathVariable("deviceId") Integer deviceId);

    @PostMapping
    RegisterDeviceResponse registerDevice(@RequestBody RegisterDeviceRequest request);

    @DeleteMapping("{deviceId}")
    void unregisterDevice(@PathVariable("deviceId") Integer deviceId);

    @PutMapping("{deviceId}")
    void updateDevice(@PathVariable("deviceId") Integer deviceId, @RequestBody UpdateDeviceRequest request);

    @PutMapping("reserve")
    ReserveDeviceResponse reserveDevice(@RequestBody ReserveDeviceRequest request);
}

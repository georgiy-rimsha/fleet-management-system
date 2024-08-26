package dev.notenger.simulation.device;

import dev.notenger.clients.device.DeviceDTO;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class DeviceDTOMapper implements Function<Device, DeviceDTO> {
    @Override
    public DeviceDTO apply(Device device) {
        return new DeviceDTO(
                device.getID(),
                device.getSerialNumber(),
                device.getAverageSpeed(),
                device.getAvailable());
    }
}

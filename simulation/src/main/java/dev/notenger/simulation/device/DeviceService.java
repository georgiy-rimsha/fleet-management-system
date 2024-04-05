package dev.notenger.simulation.device;

import com.anylogic.engine.Point;
import com.notenger.model.SimulationClient;
import dev.notenger.clients.device.DeviceDTO;
import dev.notenger.clients.device.InvalidVehicleIdException;
import dev.notenger.clients.device.NoDeviceAvailableException;
import dev.notenger.simulation.place.Place;
import dev.notenger.simulation.place.PlaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Slf4j
@RequiredArgsConstructor
@Service
public class DeviceService {

    private final SimulationClient simulationClient;
    private final PlaceService placeService;

    public Device registerDevice(String placeName, Double averageSpeed) {
        Place place = placeService.getPlaceByName(placeName);
        Device device = Device.builder()
                .lastLocation(getRandomNearPoint(place.getLatitude(), place.getLongitude()))
                .averageSpeed(averageSpeed)
                .build();
        simulationClient.addDeviceAgent(device);
        device.noteAvailable();
        device.onArrival();

        return device;
    }

    private Point getRandomNearPoint(double latitude, double longitude) {
        Point originalPoint = new Point().setLatLon(latitude, longitude);
        Random random = new Random();
        final double range = 0.005;
        double latOffset = -range + (random.nextDouble() * (2 * range));
        double lonOffset = -range + (random.nextDouble() * (2 * range));
        Point offset = new Point().setLatLon(latOffset, lonOffset);

        return originalPoint.add(offset);
    }

    public void unregisterDevice(Integer id) {
        simulationClient.removeDeviceAgentById(id);
    }

    public void updateDevice(Integer id, Double averageSpeed) {
        Device device = (Device) simulationClient.findDeviceAgentById(id).orElseThrow();
        device.setAverageSpeed(averageSpeed);
    }

    public Device reserveAndGetAvailableDevice(Integer vehicleId) {
        if (vehicleId == null) {
            throw new InvalidVehicleIdException("vehicle id is required");
        }
        Device device = (Device) simulationClient.findAllAvailableDeviceAgents()
                .stream()
                .findAny()
                .orElseThrow(() -> new NoDeviceAvailableException(
                        "no available devices found"
                ));

        device.reserveForVehicle(vehicleId);
        return device;
    }

    public List<DeviceDTO> getAllDevices() {
        return simulationClient.findAllDeviceAgents().stream().map(d -> (Device) d)
                .map(d -> new DeviceDTO(
                    d.getID(),
                    d.getVehicleId(),
                    d.getSerialNumber(),
                    d.getAverageSpeed(),
                    d.getAvailable()))
                .toList();
    }

    public DeviceDTO getDevice(Integer id) {
        return simulationClient.findAllDeviceAgents().stream().map(d -> (Device) d).map(d -> new DeviceDTO(
                d.getID(),
                d.getVehicleId(),
                d.getSerialNumber(),
                d.getAverageSpeed(),
                d.getAvailable())).findAny().orElseThrow();
    }
}

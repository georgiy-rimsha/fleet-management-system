package dev.notenger.simulation.device;

import com.anylogic.engine.Point;
import dev.notenger.clients.device.DeviceDTO;
import dev.notenger.clients.device.exception.DeviceLimitReachedException;
import dev.notenger.clients.device.exception.DeviceNotFoundException;
import dev.notenger.clients.vehicle.exception.VehicleNotFoundException;
import dev.notenger.simulation.model.SimulationClient;
import dev.notenger.simulation.place.Place;
import dev.notenger.simulation.place.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@RequiredArgsConstructor
@Service
public class DeviceService {

    private final SimulationClient simulationClient;
    private final PlaceService placeService;
    private final DeviceDTOMapper deviceDTOMapper;
    @Value("${device.limit:5}")
    private int deviceLimit;

    public void registerDevice(String serialNumber) {
        checkIfLimitReached();
        Device device = Device.builder()
                .serialNumber(serialNumber)
                .build();
        simulationClient.addDeviceAgent(device);
    }

    public void attachDevice(Integer deviceId, String placeName, Double averageSpeed) {
        Place place = placeService.getPlaceByName(placeName);
        Device device = (Device) simulationClient.findDeviceAgentById(deviceId)
                .orElseThrow(() -> new DeviceNotFoundException(
                    "device with id [%s] not found".formatted(deviceId)
                ));
        device.reset();
        device.setAverageSpeed(averageSpeed);
        device.setLastLocation(getRandomNearPoint(place.getLatitude(), place.getLongitude()));
        device.setLastVisitedPlace(place);
        device.noteUnavailable();

        device.activate();
    }

    public void detachDevice(Integer deviceId) {
        Device device = (Device) simulationClient.findDeviceAgentById(deviceId)
                .orElseThrow(() -> new VehicleNotFoundException(
                        "device with id [%s] not found".formatted(deviceId)
                ));
        device.noteAvailable();
        device.deactivate();
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

    public void deregisterDevice(Integer deviceId) {
        checkIfDeviceExistsOrThrow(deviceId);
        simulationClient.removeDeviceAgentById(deviceId);
    }

    private void checkIfDeviceExistsOrThrow(Integer deviceId) {
        if (!simulationClient.existsDeviceAgentById(deviceId)) {
            throw new DeviceNotFoundException(
                    "device with id [%s] not found".formatted(deviceId)
            );
        }
    }

    private void checkIfLimitReached() {
        if (simulationClient.findAllDeviceAgents().size() == deviceLimit) {
            throw new DeviceLimitReachedException(
                    "Device limit of %s reached. Please delete existing vehicles to free up device slots".formatted(deviceLimit)
            );
        }
    }

    public void updateDevice(Integer deviceId, Double averageSpeed) {
        Device device = (Device) simulationClient.findDeviceAgentById(deviceId)
                                        .orElseThrow(() -> new DeviceNotFoundException(
                                            "device with id [%s] not found".formatted(deviceId)
                                        ));
        device.setAverageSpeed(averageSpeed);
    }

    public List<DeviceDTO> getAllAvailableDevices() {
        return simulationClient.findAllAvailableDeviceAgents()
                                    .stream()
                                    .map(d -> (Device) d)
                                    .map(deviceDTOMapper)
                                    .toList();
    }

    public List<DeviceDTO> getAllDevices() {
        return simulationClient.findAllDeviceAgents()
                                    .stream()
                                    .map(d -> (Device) d)
                                    .map(deviceDTOMapper)
                                    .toList();
    }

    public DeviceDTO getDevice(Integer deviceId) {
        return simulationClient.findDeviceAgentById(deviceId)
                                    .map(d -> (Device) d)
                                    .map(deviceDTOMapper)
                                    .orElseThrow(() -> new DeviceNotFoundException(
                                            "device with id [%s] not found".formatted(deviceId)
                                    ));
    }
}

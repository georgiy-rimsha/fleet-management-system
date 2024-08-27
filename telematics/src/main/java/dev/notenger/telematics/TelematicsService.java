package dev.notenger.telematics;

import dev.notenger.clients.telematics.exception.TelemetryNotFoundException;
import dev.notenger.telematics.messaging.GeolocationDTO;
import dev.notenger.telematics.messaging.TelemetryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TelematicsService {

    private final MongoTemplate mongoTemplate;
    private final SimpMessagingTemplate messagingTemplate;
    private final GeolocationDTOMapper geolocationDTOMapper;

    public void saveAndPublish(Telemetry telemetry) {
        mongoTemplate.insert(telemetry);
        GeolocationDTO geolocationDTO = geolocationDTOMapper.apply(telemetry);
        messagingTemplate.convertAndSend("/telematics/geolocation", geolocationDTO);
    }

    @Scheduled(fixedRate = 1_000)
    private void pushAggregatedData() {
        TelemetryDTO telemetryDTO = getDeviceTelemetrySummary();
        messagingTemplate.convertAndSend("/telematics/telemetry", telemetryDTO);
    }

    private TelemetryDTO getDeviceTelemetrySummary() {
        List<Telemetry> telemetryList = mongoTemplate.findAll(Telemetry.class);
        Map<Integer, Telemetry> latestTelemetryByDeviceId = telemetryList.stream()
                .collect(
                        Collectors.toMap(
                                Telemetry::getDeviceId,
                                Function.identity(),
                                BinaryOperator.maxBy(Comparator.comparing(Telemetry::getTimestamp))));

        OptionalDouble totalAverageSpeed = latestTelemetryByDeviceId.values()
                .stream()
                .mapToDouble(Telemetry::getSpeedometer)
                .average();
        OptionalDouble totalAverageOdometer = latestTelemetryByDeviceId.values()
                .stream()
                .mapToDouble(Telemetry::getOdometer)
                .average();
        OptionalDouble totalAverageFuelGauge = latestTelemetryByDeviceId.values()
                .stream()
                .mapToDouble(Telemetry::getFuelGauge)
                .average();

        return new TelemetryDTO(
                totalAverageSpeed.isPresent() ? totalAverageSpeed.getAsDouble() : null,
                totalAverageOdometer.isPresent() ? totalAverageOdometer.getAsDouble() : null,
                totalAverageFuelGauge.isPresent() ? totalAverageFuelGauge.getAsDouble() : null);
    }

    public Double getLastOdometerReadingByDeviceId(Integer deviceId) {
        List<Telemetry> telemetryList = mongoTemplate.findAll(Telemetry.class);

        Telemetry lastRecord = telemetryList
            .stream()
            .filter(telemetry -> Objects.equals(telemetry.getDeviceId(), deviceId))
            .max(Comparator.comparing(Telemetry::getTimestamp))
            .orElseThrow(() -> new TelemetryNotFoundException(
                    "no telemetry found for device with id [%s]".formatted(deviceId)
            ));

        return lastRecord.getOdometer();
    }

}

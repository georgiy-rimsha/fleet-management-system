package dev.notenger.telematics;

import dev.notenger.clients.telematics.exception.TelemetryNotFoundException;
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

    public void save(Telemetry telemetry) {
        mongoTemplate.insert(telemetry);
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

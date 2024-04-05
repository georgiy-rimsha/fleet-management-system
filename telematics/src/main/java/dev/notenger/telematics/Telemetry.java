package dev.notenger.telematics;

import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("telemetry")
@NoArgsConstructor
@Data
public class Telemetry {
    @Id
    private String id;
    private Integer deviceId;
    private Integer vehicleId;
    private double latitude;
    private double longitude;
    private double heading;
    private List<List<Double>> pathData;
    private double speedometer;
    private double odometer;
    private double fuelGauge;
    private double timestamp;
}

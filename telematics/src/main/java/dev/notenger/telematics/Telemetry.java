package dev.notenger.telematics;

import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document("telemetry")
@NoArgsConstructor
@Data
public class Telemetry {
    @Id
    private String id;
    private Integer deviceId;
    private Double latitude;
    private Double longitude;
    private Double heading;
    private List<List<Double>> pathData;
    private Double speedometer;
    private Double odometer;
    private Double fuelGauge;
    private LocalDateTime timestamp;
}

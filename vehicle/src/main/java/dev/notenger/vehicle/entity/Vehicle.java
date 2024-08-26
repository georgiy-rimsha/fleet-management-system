package dev.notenger.vehicle.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@Entity
@Table(
        name = "vehicles",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "vin_unique",
                        columnNames = "vin"
                )
        }
)
@NoArgsConstructor
public class Vehicle extends BaseEntity {

    @Column(nullable = false)
    private String vin;

    @Column(nullable = false)
    private String make;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false)
    private String groupName;

    @Column(nullable = false)
    private Double averageSpeed;

    @Column(nullable = false)
    private Integer deviceId;

    @Column
    private Double lastOdometerReading;
}

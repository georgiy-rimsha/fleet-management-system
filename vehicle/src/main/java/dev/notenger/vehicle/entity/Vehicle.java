package dev.notenger.vehicle.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
@Entity
@ToString(callSuper=true)
@Table(name = "vehicles")
@NoArgsConstructor
public class Vehicle extends BaseEntity {
    private String vin;
    private String make;
    private String model;
    private Integer year;
    private Integer deviceId;
}

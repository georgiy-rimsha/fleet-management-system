package dev.notenger.vehicle.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@SuperBuilder
@Entity
@ToString(callSuper=true)
@Table(name = "vehicles")
@NoArgsConstructor
public class Vehicle extends BaseEntity {
    private String vinNumber;
    private String make;
    private String model;
    private Integer year;
    private Integer deviceId;
}

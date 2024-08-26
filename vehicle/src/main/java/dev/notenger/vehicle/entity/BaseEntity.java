package dev.notenger.vehicle.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@MappedSuperclass
@Data
@SuperBuilder
@NoArgsConstructor
public class BaseEntity implements Serializable {
    @Id
    @SequenceGenerator(
            name = "vehicles_id_seq",
            sequenceName = "vehicles_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "vehicles_id_seq"
    )
    private Integer id;

}

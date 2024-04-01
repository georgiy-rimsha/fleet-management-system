package dev.notenger.vehicle.repository;

import com.github.javafaker.Faker;
import dev.notenger.AbstractTestcontainers;
import dev.notenger.vehicle.entity.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class VehicleRepositoryTest extends AbstractTestcontainers {

    @Autowired
    private VehicleRepository underTest;

    @Autowired
    private ApplicationContext applicationContext;

    protected static final Faker FAKER = new Faker();

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
        System.out.println(applicationContext.getBeanDefinitionCount());
    }

    @Test
    void existsVehicleById() {
        // Given
        String model = FAKER.internet().domainName() + "-" + UUID.randomUUID();
        Vehicle vehicle = Vehicle
                .builder()
                .year(1999)
                .model(model)
                .build();

        underTest.save(vehicle);

        int id = underTest.findAll()
                .stream()
                .filter(v -> v.getModel().equals(model))
                .map(Vehicle::getId)
                .findFirst()
                .orElseThrow();

        // When
        var actual = underTest.existsVehicleById(id);

        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsVehicleByIdFailsWhenIdNotPresent() {
        // Given
        int id = -1;

        // When
        var actual = underTest.existsVehicleById(id);

        // Then
        assertThat(actual).isFalse();
    }

    @Test
    void existsVehicleByModel() {
        // Given
        String model = FAKER.internet().domainName() + "-" + UUID.randomUUID();
        Vehicle vehicle = Vehicle
                .builder()
                .year(1999)
                .model(model)
                .build();

        underTest.save(vehicle);

        // When
        var actual = underTest.existsVehicleByModel(model);

        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsVehicleByModelFailsWhenModelNotPresent() {
        // Given
        String model = FAKER.internet().domainName() + "-" + UUID.randomUUID();

        // When
        var actual = underTest.existsVehicleByModel(model);

        // Then
        assertThat(actual).isFalse();
    }
}
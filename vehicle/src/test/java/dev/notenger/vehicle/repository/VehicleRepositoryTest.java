package dev.notenger.vehicle.repository;

import com.github.javafaker.Faker;
import dev.notenger.vehicle.AbstractTestcontainers;
import dev.notenger.vehicle.entity.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class VehicleRepositoryTest extends AbstractTestcontainers {

    @Autowired
    private VehicleRepository underTest;

    @Autowired
    private ApplicationContext applicationContext;

    private static final Random RANDOM = new Random();
    protected static final Faker FAKER = new Faker();

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
        System.out.println(applicationContext.getBeanDefinitionCount());
    }

    @Test
    void existsVehicleById() {
        // Given
        String fakeVIN = FAKER.bothify("1##?#??######");
        String fakeMake = FAKER.company().name();
        String fakeModel = FAKER.lorem().word();
        Integer fakeYear = RANDOM.nextInt(1990, 2023);
        String fakeGroupName = FAKER.address().city();
        Double averageSpeed = RANDOM.nextDouble(200);
        Integer fakeDeviceId = RANDOM.nextInt(100);

        Vehicle vehicle = Vehicle
                .builder()
                .vin(fakeVIN)
                .make(fakeMake)
                .model(fakeModel)
                .year(fakeYear)
                .groupName(fakeGroupName)
                .averageSpeed(averageSpeed)
                .deviceId(fakeDeviceId)
                .build();

        underTest.save(vehicle);

        int id = underTest.findAll()
                .stream()
                .filter(v -> v.getVin().equals(fakeVIN))
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
    void existsVehicleByVin() {
        // Given
        String fakeVIN = FAKER.bothify("1##?#??######");
        String fakeMake = FAKER.company().name();
        String fakeModel = FAKER.lorem().word();
        Integer fakeYear = RANDOM.nextInt(1990, 2023);
        String fakeGroupName = FAKER.address().city();
        Double averageSpeed = RANDOM.nextDouble(200);
        Integer fakeDeviceId = RANDOM.nextInt(100);

        Vehicle vehicle = Vehicle
                .builder()
                .vin(fakeVIN)
                .make(fakeMake)
                .model(fakeModel)
                .year(fakeYear)
                .groupName(fakeGroupName)
                .averageSpeed(averageSpeed)
                .deviceId(fakeDeviceId)
                .build();

        underTest.save(vehicle);

        // When
        var actual = underTest.existsVehicleByVin(fakeVIN);

        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsVehicleByVinFailsWhenVinNotPresent() {
        // Given
        String fakeVIN = FAKER.bothify("1##?#??######");

        // When
        var actual = underTest.existsVehicleByVin(fakeVIN);

        // Then
        assertThat(actual).isFalse();
    }
}
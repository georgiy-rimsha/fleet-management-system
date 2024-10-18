package dev.notenger.vehicle.journey;

import com.github.javafaker.Faker;
import dev.notenger.clients.device.AttachDeviceRequest;
import dev.notenger.clients.device.DeviceClient;
import dev.notenger.clients.vehicle.AddVehicleRequest;
import dev.notenger.clients.vehicle.UpdateVehicleRequest;
import dev.notenger.vehicle.entity.Vehicle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestPropertySource(properties = "eureka.client.enabled=false")
@AutoConfigureWebTestClient
public class VehicleIT {

    @Autowired
    private WebTestClient webTestClient;
    @MockBean
    private DeviceClient deviceClient;
    private static final Random RANDOM = new Random();
    protected static final Faker FAKER = new Faker();
    private static final String VEHICLE_PATH = "/api/v1/vehicles";

    @Test
    void canCreateVehicle() {
        // given
        doNothing().when(deviceClient).attachDevice(anyInt(), any(AttachDeviceRequest.class));

        // create registration request
        String fakeVIN = FAKER.bothify("1##?#??######");
        String fakeMake = FAKER.company().name();
        String fakeModel = FAKER.lorem().word();
        Integer fakeYear = RANDOM.nextInt(1990, 2023);
        String fakeGroupName = FAKER.address().city();
        Double averageSpeed = RANDOM.nextDouble(200);
        Integer fakeDeviceId = RANDOM.nextInt(100);

        AddVehicleRequest request = new AddVehicleRequest(
                fakeVIN, fakeMake, fakeModel, fakeYear, fakeGroupName, averageSpeed, fakeDeviceId
        );
        // send a post request
        webTestClient.post()
                .uri(VEHICLE_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), AddVehicleRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get all vehicles
        List<Vehicle> allVehicles = webTestClient.get()
                .uri(VEHICLE_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Vehicle>() {
                })
                .returnResult()
                .getResponseBody();

        int id = allVehicles.stream()
                .filter(vehicle -> vehicle.getVin().equals(fakeVIN))
                .map(Vehicle::getId)
                .findFirst()
                .orElseThrow();

        // make sure that vehicle is present
        Vehicle expectedVehicle = Vehicle
                .builder()
                .id(id)
                .vin(fakeVIN)
                .make(fakeMake)
                .model(fakeModel)
                .year(fakeYear)
                .groupName(fakeGroupName)
                .averageSpeed(averageSpeed)
                .deviceId(fakeDeviceId)
                .build();

        assertThat(allVehicles).contains(expectedVehicle);

        // get vehicle by id
        webTestClient.get()
                .uri(VEHICLE_PATH + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Vehicle>() {
                })
                .isEqualTo(expectedVehicle);
    }

    @Test
    void canDeleteVehicle() {
        // create registration request
        String fakeVIN = FAKER.bothify("1##?#??######");
        String fakeMake = FAKER.company().name();
        String fakeModel = FAKER.lorem().word();
        Integer fakeYear = RANDOM.nextInt(1990, 2023);
        String fakeGroupName = FAKER.address().city();
        Double averageSpeed = RANDOM.nextDouble(200);
        Integer fakeDeviceId = RANDOM.nextInt(100);

        AddVehicleRequest request = new AddVehicleRequest(
                fakeVIN, fakeMake, fakeModel, fakeYear, fakeGroupName, averageSpeed, fakeDeviceId
        );
        // send a post request
        webTestClient.post()
                .uri(VEHICLE_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), AddVehicleRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get all vehicles
        List<Vehicle> allVehicles = webTestClient.get()
                .uri(VEHICLE_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Vehicle>() {
                })
                .returnResult()
                .getResponseBody();

        int id = allVehicles.stream()
                .filter(vehicle -> vehicle.getVin().equals(fakeVIN))
                .map(Vehicle::getId)
                .findFirst()
                .orElseThrow();

        // make sure that vehicle is present
        Vehicle expectedVehicle = Vehicle
                .builder()
                .id(id)
                .vin(fakeVIN)
                .make(fakeMake)
                .model(fakeModel)
                .year(fakeYear)
                .groupName(fakeGroupName)
                .deviceId(fakeDeviceId)
                .averageSpeed(averageSpeed)
                .build();

        assertThat(allVehicles).contains(expectedVehicle);

        // delete vehicle by id
        webTestClient.delete()
                .uri(VEHICLE_PATH + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();

        // get vehicle by id
        webTestClient.get()
                .uri(VEHICLE_PATH + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void canUpdateVehicle() {
        // create registration request
        String fakeVIN = "1G6DC5EY3B0123456";
        String fakeMake = "Acme Motors";
        String fakeModel = "Phantom";
        Integer fakeYear = 2022;
        String fakeGroupName = "Paris";
        Double averageSpeed = RANDOM.nextDouble(200);
        Integer fakeDeviceId = RANDOM.nextInt(100);

        AddVehicleRequest request = new AddVehicleRequest(
                fakeVIN, fakeMake, fakeModel, fakeYear, fakeGroupName, averageSpeed, fakeDeviceId
        );
        // send a post request
        webTestClient.post()
                .uri(VEHICLE_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), AddVehicleRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get all vehicles
        List<Vehicle> allVehicles = webTestClient.get()
                .uri(VEHICLE_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Vehicle>() {
                })
                .returnResult()
                .getResponseBody();

        int id = allVehicles.stream()
                .filter(vehicle -> vehicle.getVin().equals(fakeVIN))
                .map(Vehicle::getId)
                .findFirst()
                .orElseThrow();

        // update vehicle by id
        String newMake = "Global Automotive";
        UpdateVehicleRequest updateRequest = new UpdateVehicleRequest(null, newMake, null, null);

        webTestClient.put()
                .uri(VEHICLE_PATH + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateRequest), UpdateVehicleRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get vehicle by id
        Vehicle updatedVehicle = webTestClient.get()
                .uri(VEHICLE_PATH + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Vehicle.class)
                .returnResult()
                .getResponseBody();

        Vehicle expectedVehicle = Vehicle
                .builder()
                .id(id)
                .vin(fakeVIN)
                .make(newMake)
                .model(fakeModel)
                .year(fakeYear)
                .groupName(fakeGroupName)
                .deviceId(fakeDeviceId)
                .averageSpeed(averageSpeed)
                .build();

        assertThat(updatedVehicle).isEqualTo(expectedVehicle);
    }
}
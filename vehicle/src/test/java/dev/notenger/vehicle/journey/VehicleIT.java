package dev.notenger.vehicle.journey;

import com.github.javafaker.Faker;
import dev.notenger.clients.vehicle.AddVehicleRequest;
import dev.notenger.clients.vehicle.UpdateVehicleRequest;
import dev.notenger.vehicle.web.VehicleDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class VehicleIT {

    @Autowired
    private WebTestClient webTestClient;
    private static final Random RANDOM = new Random();
    private static final String VEHICLE_PATH = "/api/v1/vehicles";

    @Test
    void canCreateVehicle() {
        // create registration request
        Faker faker = new Faker();
        String fakeVIN = faker.bothify("1##?#??######");
        String fakeMake = faker.company().name();
        String fakeModel = faker.lorem().word();
        Integer fakeYear = RANDOM.nextInt(1990, 2023);

        AddVehicleRequest request = new AddVehicleRequest(
                fakeVIN, fakeMake, fakeModel, fakeYear
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
        List<VehicleDTO> allVehicles = webTestClient.get()
                .uri(VEHICLE_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<VehicleDTO>() {
                })
                .returnResult()
                .getResponseBody();

        int id = allVehicles.stream()
                .filter(vehicle -> vehicle.vin().equals(fakeVIN))
                .map(VehicleDTO::id)
                .findFirst()
                .orElseThrow();

        // make sure that vehicle is present
        VehicleDTO expectedVehicle = new VehicleDTO(
          id, fakeVIN, fakeMake, fakeModel, fakeYear,null
        );

        assertThat(allVehicles).contains(expectedVehicle);

        // get vehicle by id
        webTestClient.get()
                .uri(VEHICLE_PATH + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<VehicleDTO>() {
                })
                .isEqualTo(expectedVehicle);
    }

    @Test
    void canDeleteVehicle() {
        // create registration request
        Faker faker = new Faker();
        String fakeVIN = faker.bothify("1##?#??######");
        String fakeMake = faker.company().name();
        String fakeModel = faker.lorem().word();
        Integer fakeYear = RANDOM.nextInt(1990, 2023);

        AddVehicleRequest request = new AddVehicleRequest(
                fakeVIN, fakeMake, fakeModel, fakeYear
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
        List<VehicleDTO> allVehicles = webTestClient.get()
                .uri(VEHICLE_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<VehicleDTO>() {
                })
                .returnResult()
                .getResponseBody();

        int id = allVehicles.stream()
                .filter(vehicle -> vehicle.vin().equals(fakeVIN))
                .map(VehicleDTO::id)
                .findFirst()
                .orElseThrow();

        // make sure that vehicle is present
        VehicleDTO expectedVehicle = new VehicleDTO(
                id, fakeVIN, fakeMake, fakeModel, fakeYear,null
        );

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

        AddVehicleRequest request = new AddVehicleRequest(
                fakeVIN, fakeMake, fakeModel, fakeYear
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
        List<VehicleDTO> allVehicles = webTestClient.get()
                .uri(VEHICLE_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<VehicleDTO>() {
                })
                .returnResult()
                .getResponseBody();

        int id = allVehicles.stream()
                .filter(vehicle -> vehicle.vin().equals(fakeVIN))
                .map(VehicleDTO::id)
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
        VehicleDTO updatedVehicle = webTestClient.get()
                .uri(VEHICLE_PATH + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(VehicleDTO.class)
                .returnResult()
                .getResponseBody();

        VehicleDTO expectedVehicle = new VehicleDTO(
                id, fakeVIN, newMake, fakeModel, fakeYear,null
        );

        assertThat(updatedVehicle).isEqualTo(expectedVehicle);
    }
}
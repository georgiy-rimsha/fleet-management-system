package dev.notenger.vehicle.service;

import com.github.javafaker.Faker;
import dev.notenger.clients.device.DeviceClient;
import dev.notenger.clients.telematics.TelematicsClient;
import dev.notenger.clients.vehicle.AddVehicleRequest;
import dev.notenger.clients.vehicle.UpdateVehicleRequest;
import dev.notenger.clients.vehicle.exception.DuplicateVehicleException;
import dev.notenger.clients.vehicle.exception.NoChangesDetectedException;
import dev.notenger.clients.vehicle.exception.VehicleNotFoundException;
import dev.notenger.vehicle.entity.Vehicle;
import dev.notenger.vehicle.repository.VehicleDao;
import dev.notenger.vehicle.web.VehicleDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

    VehicleService underTest;
    @Mock VehicleDao vehicleDao;
    @Mock DeviceClient deviceClient;
    VehicleDTOMapper vehicleDTOMapper = new VehicleDTOMapper();

    protected static final Faker FAKER = new Faker();
    private static final Random random = new Random();

    @BeforeEach
    void setUp() {
        underTest = new VehicleService(vehicleDao, vehicleDTOMapper, deviceClient);
    }

    @Test
    void addVehicle() {
        // Given
        String fakeVIN = FAKER.bothify("1##?#??######");
        String fakeMake = FAKER.company().name();
        String fakeModel = FAKER.lorem().word();

        when(vehicleDao.existsVehicleByVin(fakeVIN)).thenReturn(false);

        // When
        underTest.addVehicle(fakeVIN, fakeMake, fakeModel, null);

        // Then
        ArgumentCaptor<Vehicle> vehicleArgumentCaptor = ArgumentCaptor.forClass(Vehicle.class);

        verify(vehicleDao).insertVehicle(vehicleArgumentCaptor.capture());

        Vehicle capturedVehicle = vehicleArgumentCaptor.getValue();

        assertThat(capturedVehicle.getId()).isNull();
        assertThat(capturedVehicle.getVin()).isEqualTo(fakeVIN);
        assertThat(capturedVehicle.getMake()).isEqualTo(fakeMake);
        assertThat(capturedVehicle.getModel()).isEqualTo(fakeModel);
    }

    @Test
    void willThrowWhenVinExistsWhileAddingVehicle() {
        // Given
        String fakeVIN = FAKER.bothify("1##?#??######");
        String fakeMake = FAKER.company().name();
        String fakeModel = FAKER.lorem().word();

        when(vehicleDao.existsVehicleByVin(fakeVIN)).thenReturn(true);

        // When
        assertThatThrownBy(() -> underTest.addVehicle(fakeVIN, fakeMake, fakeModel, null))
                .isInstanceOf(DuplicateVehicleException.class).hasMessage("vin already taken");

        // Then
        verify(vehicleDao, never()).insertVehicle(any());
    }

    @Test
    void canGetVehicle() {
        // Given
        int id = 10;
        String fakeVIN = FAKER.bothify("1##?#??######");
        String fakeMake = FAKER.company().name();
        String fakeModel = FAKER.lorem().word();

        Vehicle vehicle = Vehicle
                .builder()
                .id(id)
                .vin(fakeVIN)
                .make(fakeMake)
                .model(fakeModel)
                .build();

        when(vehicleDao.selectVehicleById(id)).thenReturn(Optional.of(vehicle));
        VehicleDTO expected = vehicleDTOMapper.apply(vehicle);

        // When
        VehicleDTO actual = underTest.getVehicle(id);

        // Then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void willThrowWhenGetVehicleReturnEmptyOptional() {
        // Given
        int id = 10;

        when(vehicleDao.selectVehicleById(id)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> underTest.getVehicle(id))
                .isInstanceOf(VehicleNotFoundException.class).hasMessage("vehicle with id [%s] not found".formatted(id));
    }

    @Test
    void canUpdateAllVehiclesProperties() {
        // Given
        int id = 10;
        String fakeVIN = "1G6DC5EY3B0123456";
        String fakeMake = "Acme Motors";
        String fakeModel = "Phantom";
        Integer fakeYear = 2022;

        Vehicle vehicle = Vehicle
                .builder()
                .id(id)
                .vin(fakeVIN)
                .make(fakeMake)
                .model(fakeModel)
                .year(fakeYear)
                .build();

        when(vehicleDao.selectVehicleById(id)).thenReturn(Optional.of(vehicle));

        String newVIN = "2T1BU4EE3CC123456";

        when(vehicleDao.existsVehicleByVin(newVIN)).thenReturn(false);

        // When
        String newMake = "Global Automotive";
        String newModel = "Spectra";
        Integer newYear = 2018;

        underTest.updateVehicle(id, newVIN, newMake, newModel, newYear);

        // Then
        ArgumentCaptor<Vehicle> vehicleArgumentCaptor = ArgumentCaptor.forClass(Vehicle.class);

        verify(vehicleDao).updateVehicle(vehicleArgumentCaptor.capture());
        Vehicle capturedVehicle = vehicleArgumentCaptor.getValue();

        assertThat(capturedVehicle.getVin()).isEqualTo(newVIN);
        assertThat(capturedVehicle.getMake()).isEqualTo(newMake);
        assertThat(capturedVehicle.getModel()).isEqualTo(newModel);
        assertThat(capturedVehicle.getYear()).isEqualTo(newYear);
    }

    @Test
    void canUpdateOnlyVehicleVin() {
        // Given
        int id = 10;
        String fakeVIN = "1G6DC5EY3B0123456";
        String fakeMake = "Acme Motors";
        String fakeModel = "Phantom";
        Integer fakeYear = 2022;

        Vehicle vehicle = Vehicle
                .builder()
                .id(id)
                .vin(fakeVIN)
                .make(fakeMake)
                .model(fakeModel)
                .year(fakeYear)
                .build();

        when(vehicleDao.selectVehicleById(id)).thenReturn(Optional.of(vehicle));

        String newVIN = "2T1BU4EE3CC123456";

        when(vehicleDao.existsVehicleByVin(newVIN)).thenReturn(false);

        // When
        underTest.updateVehicle(id, newVIN, null, null, null);

        // Then
        ArgumentCaptor<Vehicle> vehicleArgumentCaptor = ArgumentCaptor.forClass(Vehicle.class);

        verify(vehicleDao).updateVehicle(vehicleArgumentCaptor.capture());
        Vehicle capturedVehicle = vehicleArgumentCaptor.getValue();

        assertThat(capturedVehicle.getVin()).isEqualTo(newVIN);
        assertThat(capturedVehicle.getMake()).isEqualTo(vehicle.getMake());
        assertThat(capturedVehicle.getModel()).isEqualTo(vehicle.getModel());
        assertThat(capturedVehicle.getYear()).isEqualTo(vehicle.getYear());
    }

    @Test
    void canUpdateOnlyVehicleMake() {
        // Given
        int id = 10;
        String fakeVIN = "1G6DC5EY3B0123456";
        String fakeMake = "Acme Motors";
        String fakeModel = "Phantom";
        Integer fakeYear = 2022;

        Vehicle vehicle = Vehicle
                .builder()
                .id(id)
                .vin(fakeVIN)
                .make(fakeMake)
                .model(fakeModel)
                .year(fakeYear)
                .build();

        when(vehicleDao.selectVehicleById(id)).thenReturn(Optional.of(vehicle));

        // When
        String newMake = "Global Automotive";
        underTest.updateVehicle(id, null, newMake, null, null);

        // Then
        ArgumentCaptor<Vehicle> vehicleArgumentCaptor = ArgumentCaptor.forClass(Vehicle.class);

        verify(vehicleDao).updateVehicle(vehicleArgumentCaptor.capture());
        Vehicle capturedVehicle = vehicleArgumentCaptor.getValue();

        assertThat(capturedVehicle.getVin()).isEqualTo(vehicle.getVin());
        assertThat(capturedVehicle.getMake()).isEqualTo(newMake);
        assertThat(capturedVehicle.getModel()).isEqualTo(vehicle.getModel());
        assertThat(capturedVehicle.getYear()).isEqualTo(vehicle.getYear());
    }

    @Test
    void canUpdateOnlyVehicleModel() {
        // Given
        int id = 10;
        String fakeVIN = "1G6DC5EY3B0123456";
        String fakeMake = "Acme Motors";
        String fakeModel = "Phantom";
        Integer fakeYear = 2022;

        Vehicle vehicle = Vehicle
                .builder()
                .id(id)
                .vin(fakeVIN)
                .make(fakeMake)
                .model(fakeModel)
                .year(fakeYear)
                .build();

        when(vehicleDao.selectVehicleById(id)).thenReturn(Optional.of(vehicle));

        // When
        String newModel = "Spectra";
        underTest.updateVehicle(id, null, null, newModel, null);

        // Then
        ArgumentCaptor<Vehicle> vehicleArgumentCaptor = ArgumentCaptor.forClass(Vehicle.class);

        verify(vehicleDao).updateVehicle(vehicleArgumentCaptor.capture());
        Vehicle capturedVehicle = vehicleArgumentCaptor.getValue();

        assertThat(capturedVehicle.getVin()).isEqualTo(vehicle.getVin());
        assertThat(capturedVehicle.getMake()).isEqualTo(vehicle.getMake());
        assertThat(capturedVehicle.getModel()).isEqualTo(newModel);
        assertThat(capturedVehicle.getYear()).isEqualTo(vehicle.getYear());
    }

    @Test
    void canUpdateOnlyVehicleYear() {
        // Given
        int id = 10;
        String fakeVIN = "1G6DC5EY3B0123456";
        String fakeMake = "Acme Motors";
        String fakeModel = "Phantom";
        Integer fakeYear = 2022;

        Vehicle vehicle = Vehicle
                .builder()
                .id(id)
                .vin(fakeVIN)
                .make(fakeMake)
                .model(fakeModel)
                .year(fakeYear)
                .build();

        when(vehicleDao.selectVehicleById(id)).thenReturn(Optional.of(vehicle));

        // When
        Integer newYear = 2018;
        underTest.updateVehicle(id, null, null, null, newYear);

        // Then
        ArgumentCaptor<Vehicle> vehicleArgumentCaptor = ArgumentCaptor.forClass(Vehicle.class);

        verify(vehicleDao).updateVehicle(vehicleArgumentCaptor.capture());
        Vehicle capturedVehicle = vehicleArgumentCaptor.getValue();

        assertThat(capturedVehicle.getVin()).isEqualTo(vehicle.getVin());
        assertThat(capturedVehicle.getMake()).isEqualTo(vehicle.getMake());
        assertThat(capturedVehicle.getModel()).isEqualTo(vehicle.getModel());
        assertThat(capturedVehicle.getYear()).isEqualTo(newYear);
    }

    @Test
    void willThrowWhenTryingToUpdateVehicleVinWhenAlreadyTaken() {
        // Given
        int id = 10;
        String fakeVIN = "1G6DC5EY3B0123456";
        String fakeMake = "Acme Motors";
        String fakeModel = "Phantom";
        Integer fakeYear = 2022;

        Vehicle vehicle = Vehicle
                .builder()
                .id(id)
                .vin(fakeVIN)
                .make(fakeMake)
                .model(fakeModel)
                .year(fakeYear)
                .build();

        when(vehicleDao.selectVehicleById(id)).thenReturn(Optional.of(vehicle));

        String newVIN = "2T1BU4EE3CC123456";

        when(vehicleDao.existsVehicleByVin(newVIN)).thenReturn(true);

        // When
        assertThatThrownBy(() -> underTest.updateVehicle(id, newVIN, null, null, null))
                .isInstanceOf(DuplicateVehicleException.class).hasMessage("vin already taken");

        // Then
        verify(vehicleDao, never()).updateVehicle(any());
    }

    @Test
    void willThrowWhenVehicleUpdateHasNoChanges() {
        // Given
        int id = 10;
        String fakeVIN = "1G6DC5EY3B0123456";
        String fakeMake = "Acme Motors";
        String fakeModel = "Phantom";
        Integer fakeYear = 2022;

        Vehicle vehicle = Vehicle
                .builder()
                .id(id)
                .vin(fakeVIN)
                .make(fakeMake)
                .model(fakeModel)
                .year(fakeYear)
                .build();

        when(vehicleDao.selectVehicleById(id)).thenReturn(Optional.of(vehicle));

        // When
        assertThatThrownBy(() -> underTest.updateVehicle(
                id,
                vehicle.getVin(),
                vehicle.getMake(),
                vehicle.getModel(),
                vehicle.getYear())
        ).isInstanceOf(NoChangesDetectedException.class).hasMessage("no data changes found");

        // Then
        verify(vehicleDao, never()).updateVehicle(any());
    }

    @Test
    void deleteVehicle() {
        // Given
        int id = 10;

        when(vehicleDao.existsVehicleById(id)).thenReturn(true);

        // When
        underTest.deleteVehicle(id);
        // Then
        verify(vehicleDao).deleteVehicleById(id);
    }

    @Test
    void willThrowDeleteVehicleByIdNotExists() {
        // Given
        int id = 10;

        when(vehicleDao.existsVehicleById(id)).thenReturn(false);

        // When
        assertThatThrownBy(() -> underTest.deleteVehicle(id))
                .isInstanceOf(VehicleNotFoundException.class)
                .hasMessage("vehicle with id [%s] not found".formatted(id));

        // Then
        verify(vehicleDao, never()).deleteVehicleById(id);
    }

    @Test
    void getAllVehicles() {
        // When
        underTest.getAllVehicles();

        // Then
        verify(vehicleDao).selectAllVehicles();
    }
}
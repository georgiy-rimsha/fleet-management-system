package dev.notenger.vehicle.service;

import dev.notenger.clients.device.DeviceClient;
import dev.notenger.clients.telematics.TelematicsClient;
import dev.notenger.clients.vehicle.AddVehicleRequest;
import dev.notenger.clients.vehicle.UpdateVehicleRequest;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

    VehicleService underTest;
    @Mock VehicleDao vehicleDao;
    @Mock DeviceClient deviceClient;;
    VehicleDTOMapper vehicleDTOMapper = new VehicleDTOMapper();

    @BeforeEach
    void setUp() {
        underTest = new VehicleService(vehicleDao, vehicleDTOMapper, deviceClient);
    }

    @Test
    void addVehicle() {
        // Given
        String model = "Porsche";

        when(vehicleDao.existsVehicleByModel(model)).thenReturn(false);

//        CreateVehicleRequest request = new CreateVehicleRequest(56., 67., 90., model);
        AddVehicleRequest request = new AddVehicleRequest(
                "10.", "10.", "speed", 9999
        );

        // When
//        underTest.addVehicle(request);

        // Then
        ArgumentCaptor<Vehicle> vehicleArgumentCaptor = ArgumentCaptor.forClass(Vehicle.class);

        verify(vehicleDao).insertVehicle(vehicleArgumentCaptor.capture());
//        verify(telematicsClient).registerVehicleAgent(any());

        Vehicle capturedVehicle = vehicleArgumentCaptor.getValue();

        assertThat(capturedVehicle.getId()).isNull();
        assertThat(capturedVehicle.getModel()).isEqualTo(request.model());
//        assertThat(capturedVehicle.getAverageSpeed()).isEqualTo(request.speed());
    }

    @Test
    void willThrowWhenModelExistsWhileAddingAVehicle() {
        // Given
        String model = "Porsche";

        when(vehicleDao.existsVehicleByModel(model)).thenReturn(true);

//        CreateVehicleRequest request = new CreateVehicleRequest(56., 67., 90., model);
        AddVehicleRequest request = new AddVehicleRequest(
                "10.", "10.", "speed", 9999
        );

        // When
//        assertThatThrownBy(() -> underTest.addVehicle(request)).isInstanceOf(IllegalArgumentException.class).hasMessage("model already taken");

        // Then
        verify(vehicleDao, never()).insertVehicle(any());
    }

    @Test
    void canGetVehicle() {
        // Given
        int id = 10;
        Vehicle vehicle = Vehicle
                .builder()
                .id(id)
                .year(1999)
                .model("Cruiser")
                .make("Toyota")
                .build();
        when(vehicleDao.selectVehicleById(id)).thenReturn(Optional.of(vehicle));
        VehicleDTO expected = vehicleDTOMapper.apply(vehicle);

        // When
        VehicleDTO actual = underTest.getVehicle(id);

        // Then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void willThrowWhenGetCustomerReturnEmptyOptional() {
        // Given
        int id = 10;

        when(vehicleDao.selectVehicleById(id)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> underTest.getVehicle(id)).isInstanceOf(IllegalArgumentException.class).hasMessage("vehicle with id [%s] not found".formatted(id));
    }

    @Test
    void canUpdateAllVehiclesProperties() {
        // Given
        int id = 10;
        Vehicle vehicle = Vehicle
                .builder()
                .id(id)
                .year(1999)
                .model("Cruiser")
                .make("Toyota")
                .build();
        when(vehicleDao.selectVehicleById(id)).thenReturn(Optional.of(vehicle));

        String newModel = "Forester";

//        UpdateVehicleRequest updateRequest = new UpdateVehicleRequest(120., "Subaru", newModel);
        UpdateVehicleRequest updateRequest = new UpdateVehicleRequest(
                null, "newMake", null
        );

        when(vehicleDao.existsVehicleByModel(newModel)).thenReturn(false);

        // When
//        underTest.updateVehicle(id, updateRequest);

        // Then
        ArgumentCaptor<Vehicle> vehicleArgumentCaptor = ArgumentCaptor.forClass(Vehicle.class);

        verify(vehicleDao).updateVehicle(vehicleArgumentCaptor.capture());
        Vehicle capturedVehicle = vehicleArgumentCaptor.getValue();

        assertThat(capturedVehicle.getModel()).isEqualTo(updateRequest.model());
        assertThat(capturedVehicle.getMake()).isEqualTo(updateRequest.make());
//        assertThat(capturedVehicle.getAverageSpeed()).isEqualTo(updateRequest.speed());
    }

    @Test
    void canUpdateOnlyVehicleSpeed() {
        // Given
        int id = 10;
        Vehicle vehicle = Vehicle
                .builder()
                .id(id)
                .year(1999)
                .model("Cruiser")
                .make("Toyota")
                .build();
        when(vehicleDao.selectVehicleById(id)).thenReturn(Optional.of(vehicle));

//        UpdateVehicleRequest updateRequest = new UpdateVehicleRequest(120., null, null);

        // When
//        underTest.updateVehicle(id, updateRequest);

        // Then
        ArgumentCaptor<Vehicle> vehicleArgumentCaptor = ArgumentCaptor.forClass(Vehicle.class);

        verify(vehicleDao).updateVehicle(vehicleArgumentCaptor.capture());
        Vehicle capturedVehicle = vehicleArgumentCaptor.getValue();

        assertThat(capturedVehicle.getModel()).isEqualTo(vehicle.getModel());
        assertThat(capturedVehicle.getMake()).isEqualTo(vehicle.getMake());
//        assertThat(capturedVehicle.getAverageSpeed()).isEqualTo(updateRequest.speed());
    }

    @Test
    void canUpdateOnlyVehicleMake() {
        // Given
        int id = 10;
        Vehicle vehicle = Vehicle
                .builder()
                .id(id)
                .year(1999)
                .model("Cruiser")
                .make("Toyota")
                .build();
        when(vehicleDao.selectVehicleById(id)).thenReturn(Optional.of(vehicle));

        UpdateVehicleRequest updateRequest = new UpdateVehicleRequest(null, "Subaru", null);

        // When
//        underTest.updateVehicle(id, updateRequest);

        // Then
        ArgumentCaptor<Vehicle> vehicleArgumentCaptor = ArgumentCaptor.forClass(Vehicle.class);

        verify(vehicleDao).updateVehicle(vehicleArgumentCaptor.capture());
        Vehicle capturedVehicle = vehicleArgumentCaptor.getValue();

        assertThat(capturedVehicle.getModel()).isEqualTo(vehicle.getModel());
        assertThat(capturedVehicle.getMake()).isEqualTo(updateRequest.make());
//        assertThat(capturedVehicle.getAverageSpeed()).isEqualTo(vehicle.getAverageSpeed());
    }

    @Test
    void canUpdateOnlyVehicleModel() {
        // Given
        int id = 10;
        Vehicle vehicle = Vehicle
                .builder()
                .id(id)
                .year(1999)
                .model("Cruiser")
                .make("Toyota")
                .build();
        when(vehicleDao.selectVehicleById(id)).thenReturn(Optional.of(vehicle));

        String newModel = "Forester";

//        UpdateVehicleRequest updateRequest = new UpdateVehicleRequest(null, null, newModel);

        when(vehicleDao.existsVehicleByModel(newModel)).thenReturn(false);

        // When
//        underTest.updateVehicle(id, updateRequest);

        // Then
        ArgumentCaptor<Vehicle> vehicleArgumentCaptor = ArgumentCaptor.forClass(Vehicle.class);

        verify(vehicleDao).updateVehicle(vehicleArgumentCaptor.capture());
        Vehicle capturedVehicle = vehicleArgumentCaptor.getValue();

//        assertThat(capturedVehicle.getModel()).isEqualTo(updateRequest.model());
        assertThat(capturedVehicle.getMake()).isEqualTo(vehicle.getMake());
//        assertThat(capturedVehicle.getAverageSpeed()).isEqualTo(vehicle.getAverageSpeed());
    }

    @Test
    void willThrowWhenTryingToUpdateVehicleModelWhenAlreadyTaken() {
        // Given
        int id = 10;
        Vehicle vehicle = Vehicle
                .builder()
                .id(id)
                .year(1999)
                .model("Cruiser")
                .make("Toyota")
                .build();
        when(vehicleDao.selectVehicleById(id)).thenReturn(Optional.of(vehicle));

        String newModel = "Forester";

//        UpdateVehicleRequest updateRequest = new UpdateVehicleRequest(null, null, newModel);

        when(vehicleDao.existsVehicleByModel(newModel)).thenReturn(true);

        // When
//        assertThatThrownBy(() -> underTest.updateVehicle(id, updateRequest)).isInstanceOf(IllegalArgumentException.class).hasMessage("model already taken");

        // Then
        verify(vehicleDao, never()).updateVehicle(any());
    }

    @Test
    void willThrowWhenVehicleUpdateHasNoChanges() {
        // Given
        int id = 10;
        Vehicle vehicle = Vehicle
                .builder()
                .id(id)
                .year(1999)
                .model("Cruiser")
                .make("Toyota")
                .build();
        when(vehicleDao.selectVehicleById(id)).thenReturn(Optional.of(vehicle));

//        UpdateVehicleRequest updateRequest = new UpdateVehicleRequest(vehicle.getAverageSpeed(), vehicle.getMake(), vehicle.getModel());

        // When
//        assertThatThrownBy(() -> underTest.updateVehicle(id, updateRequest)).isInstanceOf(IllegalArgumentException.class).hasMessage("no data changes found");

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
        assertThatThrownBy(() -> underTest.deleteVehicle(id)).isInstanceOf(IllegalArgumentException.class).hasMessage("vehicle with id [%s] not found".formatted(id));

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
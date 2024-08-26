package dev.notenger.simulation.device;

import com.anylogic.engine.Point;
import dev.notenger.simulation.model.DeviceAgent;
import dev.notenger.simulation.model.GISPlace;
import dev.notenger.simulation.model.SimulationMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Random;

@Data
@Builder
@AllArgsConstructor
@ToString
public class Device extends DeviceAgent {
    private Integer ID;
    private String serialNumber;
    private Double averageSpeed;
    private Boolean available;
    private double odometer;
    private double fuelGauge;
    private double speedometer;
    private Point lastLocation;
    private GISPlace lastVisitedPlace;

    private static final double BASE_CONSUMPTION_RATE = 0.06;
    private static final double SPEED_LIMIT = 90;
    private static final double RANDOM_FACTOR = 0.1;
    private static final int HUNDRED_KPH = 100;

    private static final Random random = new Random();

    @Override
    public void onSensorsDataReceived() {
        if (available) return;
        updateGauges();
        SimulationMessage message = new SimulationMessage(
                    ID,
                    getLatitude(),
                    getLongitude(),
                    getGISHeading(),
                    pathData,
                    speedometer,
                    odometer,
                    fuelGauge,
                    LocalDateTime.now());
        sendMessage(message);
    }

    public void updateGauges() {
        Point currentLocation = new Point().setLatLon(getLatitude(), getLongitude());
        double distanceTraveled = calculateDistance(lastLocation, currentLocation);
        double actualSpeed = getSpeed(KPH) / HUNDRED_KPH;

        updateOdometer(distanceTraveled);
        updateFuelGauge(distanceTraveled, actualSpeed);
        updateSpeedometer(actualSpeed);

        lastLocation = currentLocation;
    }

    private void updateFuelGauge(double distanceKm, double actualSpeed) {
        fuelGauge += calculateFuelConsumption(distanceKm, actualSpeed);
    }

    private void updateOdometer(double distanceKm) {
        odometer += distanceKm;
    }

    private void updateSpeedometer(double actualSpeed) {
        speedometer = actualSpeed;
    }

    private double calculateFuelConsumption(double distanceKm, double speedKmPerHour) {
        double consumptionRate = calculateConsumptionRate(speedKmPerHour);
        return distanceKm * consumptionRate;
    }

    private double calculateConsumptionRate(double speedKmPerHour) {
        double randomFactor = (random.nextDouble() - 0.5) * 2 * RANDOM_FACTOR;
        double baseRate = BASE_CONSUMPTION_RATE * (1 + randomFactor);

        double logSpeedCoefficient = speedKmPerHour > SPEED_LIMIT ?
                Math.log(speedKmPerHour) / Math.log(SPEED_LIMIT) : 1;
        return baseRate * logSpeedCoefficient;
    }

    private double calculateDistance(Point lastLocation, Point currentLocation) {
        return currentLocation.distanceGIS(lastLocation, KILOMETER);
    }

    @Override
    public void onChangeSpeed() {
        changeSpeed();
    }

    public void changeSpeed() {
        if (available) return;
        double randomFactor = (random.nextDouble() - 0.5) * 2 * RANDOM_FACTOR;
        double speed = averageSpeed * (1 + randomFactor) * HUNDRED_KPH;
        setSpeed(speed, KPH);
    }

    @Override
    public void onArrival() {
        moveToNextRandom();
        updatePathData();
    }

    @Override
    public void onStartup() {
        noteAvailable();
    }

    public void noteAvailable() {
        this.available = true;
    }

    public void noteUnavailable() {
        this.available = false;
    }

    public void reset() {
        this.odometer = 0;
        this.fuelGauge = 0;
        this.speedometer = 0;
    }

    public void activate() {
        setLocation(lastLocation);
        onArrival();
    }

    public void deactivate() {
        stop();
    }
}

package dev.notenger.simulation.device;

import com.anylogic.engine.IPathData;
import com.anylogic.engine.Point;
import com.anylogic.engine.markup.Curve;
import com.anylogic.engine.markup.GISCurve;
import com.notenger.model.DeviceAgent;
import com.notenger.model.SimulationMessage;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
@ToString
@Slf4j
public class Device extends DeviceAgent {
    private Integer ID;
    private Integer vehicleId;
    private String serialNumber;
    private Double averageSpeed;
    private Boolean available;
    private double odometer;
    private double fuelGauge;
    private double speedometer;
    private Point lastLocation;
    private LocalDateTime lastTime;

    private static final double BASE_CONSUMPTION_RATE = 0.06;
    private static final double SPEED_LIMIT = 90;
    private static final double RANDOM_FACTOR = 0.1;

    private static final Random random = new Random();

    @Override
    public void onSensorsDataReceived() {
        updateGauges();
        SimulationMessage message = new SimulationMessage(
                    ID,
                    vehicleId,
                    getLatitude(),
                    getLongitude(),
                    getGISHeading(),
                    pathData,
                    speedometer,
                    odometer,
                    fuelGauge,
                    time());
        sendMessage(message);
    }

    public void updateGauges() {
        Point currentLocation = new Point().setLatLon(getLatitude(), getLongitude());
        double distanceTraveled = calculateDistance(lastLocation, currentLocation); // KM
//        double timeTaken = calculateTime(lastTime, currentTime); // sec
        double actualSpeed = getSpeed(KPH); // calculateSpeed(distanceTraveled, timeTaken);

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

    private double calculateTime(LocalDateTime lastTime, LocalDateTime currentTime) {
        Duration duration = Duration.between(lastTime, currentTime);
        return duration.toMillis() / (1000.0 * 60 * 60);
    }

    private double calculateSpeed(double distanceKm, double timeTaken) {
        if (timeTaken <= 0) {
            return 0;
        }
        return distanceKm / timeTaken;
    }

    @Override
    public void onChangeSpeed() {
        changeSpeed();
    }

    public void changeSpeed() {
        double randomFactor = (random.nextDouble() - 0.5) * 2 * RANDOM_FACTOR;
        double speed = averageSpeed * (1 + randomFactor);
        setSpeed(speed, KPH);
    }

    @Override
    public void onArrival() {
        moveToNextRandom();
        updatePathData();
    }

    @Override
    public void onStartup() {
        setLocation(lastLocation);
    }

    public void noteAvailable() {
        this.available = true;
    }

    public void reserveForVehicle(Integer vehicleId) {
        this.vehicleId = vehicleId;
        this.available = false;
    }
}

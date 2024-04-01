package dev.notenger.simulation.place;

public record AddPlaceRequest(
        String name,
        Double latitude,
        Double longitude) {
}

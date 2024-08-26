package dev.notenger.clients.place;

public record AddPlaceRequest(
        String name,
        Double latitude,
        Double longitude) {
}

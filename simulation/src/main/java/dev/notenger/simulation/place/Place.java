package dev.notenger.simulation.place;

import dev.notenger.simulation.model.GISPlace;
import lombok.Getter;

@Getter
public class Place extends GISPlace {
    private final String name;

    public Place(String name, double latitude, double longitude) {
        super(latitude, longitude);
        this.name = name;
    }
}

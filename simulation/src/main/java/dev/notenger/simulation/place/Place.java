package dev.notenger.simulation.place;

import com.anylogic.engine.Point;
import com.anylogic.engine.markup.GISPoint;
import com.notenger.model.GISPlace;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
public class Place extends GISPlace {
    private final String name;

    public Place(String name, double latitude, double longitude) {
        super(latitude, longitude);
        this.name = name;
    }
}

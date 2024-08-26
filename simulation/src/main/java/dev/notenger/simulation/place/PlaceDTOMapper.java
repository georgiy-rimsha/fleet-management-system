package dev.notenger.simulation.place;

import dev.notenger.clients.place.PlaceDTO;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class PlaceDTOMapper implements Function<Place, PlaceDTO> {
    @Override
    public PlaceDTO apply(Place place) {
        return new PlaceDTO(
                place.getName(),
                place.getLatitude(),
                place.getLongitude());
    }
}

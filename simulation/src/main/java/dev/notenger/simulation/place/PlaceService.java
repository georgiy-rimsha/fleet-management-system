package dev.notenger.simulation.place;

import dev.notenger.clients.place.PlaceDTO;
import dev.notenger.clients.place.exception.DuplicatePlaceException;
import dev.notenger.clients.place.exception.PlaceNotFoundException;
import dev.notenger.simulation.model.SimulationClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final SimulationClient simulationClient;
    private final PlaceDTOMapper placeDTOMapper;

    public void addPlace(String name, Double latitude, Double longitude) {
        if (simulationClient.findGISPlaceByName(name).isPresent()) {
            throw new DuplicatePlaceException(
                    "place with name [%s] already exists".formatted(name)
            );
        }

        Place place = new Place(name, latitude, longitude);
        simulationClient.addGISPlace(place);
    }

    public List<PlaceDTO> getAllPlaces() {
        return simulationClient.findAllGISPlaces()
                .stream()
                .map(p -> (Place) p)
                .map(placeDTOMapper)
                .toList();
    }

    public Place getPlaceByName(String name) {
        return (Place) simulationClient.findGISPlaceByName(name)
                .orElseThrow(() -> new PlaceNotFoundException(
                        "place with name [%s] not found".formatted(name)
                ));
    }

}

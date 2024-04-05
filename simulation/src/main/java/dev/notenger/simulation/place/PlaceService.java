package dev.notenger.simulation.place;

import com.notenger.model.SimulationClient;
import dev.notenger.clients.place.DuplicatePlaceException;
import dev.notenger.clients.place.PlaceDTO;
import dev.notenger.clients.place.PlaceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final SimulationClient simulationClient;

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
        return simulationClient.findAllGISPlaces().stream().map(p -> (Place) p)
                .map(p -> new PlaceDTO(
                        p.getName(), p.getLatitude(), p.getLongitude())
                )
                .toList();
    }

    public Place getPlaceByName(String name) {
        return (Place) simulationClient.findGISPlaceByName(name)
                .orElseThrow(() -> new PlaceNotFoundException(
                        "place with name [%s] not found".formatted(name)
                ));
    }

}

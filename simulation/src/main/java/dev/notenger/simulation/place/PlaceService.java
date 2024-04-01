package dev.notenger.simulation.place;

import com.anylogic.engine.markup.GISPoint;
import com.notenger.model.SimulationClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceService {
    private final PlaceRepository placeRepository;
    private final SimulationClient simulationClient;

    public void addPlace(String name, Double latitude, Double longitude) {
        Place place = new Place(name, latitude, longitude);
        placeRepository.save(place);
        simulationClient.addGISPlace(latitude, longitude);
    }

    public List<Place> getAllPlaces() {
        return placeRepository.findAll();
    }




}

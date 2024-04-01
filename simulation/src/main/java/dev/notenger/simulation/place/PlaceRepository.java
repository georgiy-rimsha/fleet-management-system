package dev.notenger.simulation.place;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PlaceRepository {

    private final List<Place> placeList = new ArrayList<>();

    public List<Place> findAll() {
        return placeList;
    }

    public Optional<Place> findByName(String placeName) {
        return placeList.stream().filter(place -> place.getName().equals(placeName)).findAny();
    }

    public void save(Place location) {
        placeList.add(location);
    }
}
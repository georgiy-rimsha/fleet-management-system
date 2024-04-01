package dev.notenger.simulation.place;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/places")
@CrossOrigin
@Slf4j
public class PlaceController {
    private final PlaceService placeService;

    @PostMapping
    public void addPlace(@RequestBody AddPlaceRequest request) {
        placeService.addPlace(request.name(), request.latitude(), request.longitude());
    }

    @GetMapping
    public List<Place> getPlaces() {
        return placeService.getAllPlaces();
    }

}

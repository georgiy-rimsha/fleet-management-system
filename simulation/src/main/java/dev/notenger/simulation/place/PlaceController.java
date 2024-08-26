package dev.notenger.simulation.place;

import dev.notenger.clients.place.AddPlaceRequest;
import dev.notenger.clients.place.PlaceDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/places")
@Slf4j
public class PlaceController {
    private final PlaceService placeService;

    @PostMapping
    public void addPlace(@RequestBody AddPlaceRequest request) {
        log.info("Add place request. Place's name is {}", request.name());
        placeService.addPlace(request.name(), request.latitude(), request.longitude());
    }

    @GetMapping
    public List<PlaceDTO> getPlaces() {
        log.info("Requested all places");
        return placeService.getAllPlaces();
    }

}

package dev.notenger.simulation.place;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class InitData implements CommandLineRunner {

    private final PlaceService placeService;

    @Override
    public void run(String... args) {
        placeService.addPlace("Paris", 48.856663, 2.351556);
        placeService.addPlace("Prague", 50.080345, 14.428974);
        placeService.addPlace("Rome", 41.887064, 12.504809);
        placeService.addPlace("Tallinn", 59.437425, 24.745137);
        placeService.addPlace("Berlin", 52.516259, 13.377217);
        placeService.addPlace("Budapest", 47.492587, 19.051046);
        placeService.addPlace("Marseille", 43.303145, 5.377800);
        placeService.addPlace("Geneva", 46.203705, 6.140000);
        placeService.addPlace("Lviv", 49.839371, 24.029807);
        placeService.addPlace("Sofia", 42.697846, 23.314215);
    }
}
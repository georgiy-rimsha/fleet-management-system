package dev.notenger.simulation;

import com.notenger.model.SimulationMessage;
import dev.notenger.amqp.RabbitMQMessageProducer;
import dev.notenger.simulation.device.DeviceService;
import dev.notenger.simulation.place.PlaceService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class InitData implements CommandLineRunner {

    private final PlaceService placeService;
    private final DeviceService deviceService;

    @Override
    public void run(String... args) throws Exception {

        placeService.addPlace("Paris", 48.856663, 2.351556);
        placeService.addPlace("Prague", 50.080345, 14.428974);
        placeService.addPlace("Rome", 41.887064, 12.504809);
        placeService.addPlace("Tallinn", 59.437425, 24.745137);
        placeService.addPlace("Berlin", 52.516259, 13.377217);

//        deviceService.registerDevice("C", 120);
//        deviceService.registerDevice("J", 100);

    }
}
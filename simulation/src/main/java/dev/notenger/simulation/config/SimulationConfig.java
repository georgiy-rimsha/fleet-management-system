package dev.notenger.simulation.config;

import dev.notenger.amqp.RabbitMQMessageProducer;
import dev.notenger.simulation.model.SimulationCallback;
import dev.notenger.simulation.model.SimulationClient;
import dev.notenger.simulation.model.SimulationMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Slf4j
@Configuration
public class SimulationConfig implements SimulationCallback {

    private final RabbitMQMessageProducer rabbitMQMessageProducer;

    @Bean
    public SimulationClient simulationClient() {
        final SimulationClient client = new SimulationClient();
        client.setCallback(this);
        client.setSensorCollectionFrequency(100L);
        client.start();
        return client;
    }

    @Override
    public void messageArrived(SimulationMessage message) {
        log.debug("New simulation message [{}]", message);
        rabbitMQMessageProducer.publish(
                message,
                "internal.exchange",
                "internal.telematics.routing-key"
        );
    }

}

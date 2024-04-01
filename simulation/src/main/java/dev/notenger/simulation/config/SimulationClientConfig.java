package dev.notenger.simulation.config;

import com.anylogic.engine.*;
import com.anylogic.engine.gui.ExperimentHost;
import com.anylogic.engine.gui.IExperimentHost;
import com.notenger.model.*;
import dev.notenger.amqp.RabbitMQMessageProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

@RequiredArgsConstructor
@Configuration
public class SimulationClientConfig implements SimulationCallback {

    private final RabbitMQMessageProducer rabbitMQMessageProducer;

    @Bean
    public SimulationClient simulationClient() {
        final SimulationClient client = new SimulationClient();
        client.setCallback(this);
        IExperimentHost host = new ExperimentHost(client);
        client.setup(host);
        host.launch();

        if ( client.getState() == Experiment.IDLE ) {
            client.run();
        }
        host.setPresentable(client.getEngine().getRoot());
        return client;
    }

    @Override
    public void messageArrived(SimulationMessage message) {
        rabbitMQMessageProducer.publish(
                message,
                "internal.exchange",
                "internal.notification.routing-key"
        );
    }

}

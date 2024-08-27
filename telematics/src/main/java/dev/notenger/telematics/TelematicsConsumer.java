package dev.notenger.telematics;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TelematicsConsumer {

    private final TelematicsService telematicsService;

    @RabbitListener(queues = "${rabbitmq.queues.telematics}")
    public void consumer(Telemetry telemetry) {
        log.debug("Consumed new message [{}]", telemetry);
        telematicsService.saveAndPublish(telemetry);
    }
}

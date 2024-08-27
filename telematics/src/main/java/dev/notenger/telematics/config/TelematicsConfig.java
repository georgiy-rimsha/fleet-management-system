package dev.notenger.telematics.config;

import dev.notenger.telematics.Telemetry;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableRabbit
@Configuration
@Getter
public class TelematicsConfig {

    @Value("${rabbitmq.exchanges.internal}")
    private String internalExchange;

    @Value("${rabbitmq.queues.telematics}")
    private String telematicsQueue;

    @Value("${rabbitmq.routing-keys.internal-telematics}")
    private String internalTelematicsRoutingKey;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Bean
    public TopicExchange internalTopicExchange() {
        return new TopicExchange(this.internalExchange);
    }

    @Bean
    public Queue telematicsQueue() {
        return new Queue(this.telematicsQueue);
    }

    @Bean
    public Binding internalToTelematicsBinding() {
        return BindingBuilder
                .bind(telematicsQueue())
                .to(internalTopicExchange())
                .with(this.internalTelematicsRoutingKey);
    }

    @PostConstruct
    public void createTTLIndex() {
        mongoTemplate.indexOps(Telemetry.class)
                .ensureIndex(new Index().on("timestamp", Sort.Direction.ASC)
                        .expire(5));
    }
}

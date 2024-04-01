package dev.notenger.simulation;

import dev.notenger.amqp.RabbitMQMessageProducer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication(
//        exclude = {DataSourceAutoConfiguration.class},
        scanBasePackages = {
        "dev.notenger.simulation",
        "dev.notenger.amqp",
})
@PropertySources({
        @PropertySource("classpath:clients-${spring.profiles.active}.properties")
})
public class SimulationApplication {
    public static void main(String[] args) {
        SpringApplication.run(SimulationApplication.class, args);
    }
}
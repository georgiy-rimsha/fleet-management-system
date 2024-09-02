package dev.notenger.telematics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.math.BigDecimal;

@SpringBootApplication(
        scanBasePackages = {
                "dev.notenger.telematics",
                "dev.notenger.amqp"
        },
        exclude = {DataSourceAutoConfiguration.class, RabbitAutoConfiguration.class}
)
public class TelematicsApplication {
    public static void main(String[] args) {
        SpringApplication.run(TelematicsApplication.class, args);
    }
}

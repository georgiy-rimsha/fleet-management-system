package dev.notenger.telematics;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootApplication(
        scanBasePackages = {
                "dev.notenger.telematics",
                "dev.notenger.amqp"
        }
)
public class TelematicsApplication {
    public static void main(String[] args) {
        SpringApplication.run(TelematicsApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(MongoTemplate mongoTemplate) {
        return args -> {

        };
    }

}

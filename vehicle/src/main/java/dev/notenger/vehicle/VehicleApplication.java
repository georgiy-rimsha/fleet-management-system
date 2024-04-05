package dev.notenger.vehicle;

import dev.notenger.vehicle.repository.VehicleRepository;
import dev.notenger.vehicle.entity.Vehicle;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import java.util.List;

@SpringBootApplication
@EnableFeignClients(
        basePackages = "dev.notenger.clients"
)
@ImportAutoConfiguration({FeignAutoConfiguration.class})
@PropertySources({
        @PropertySource("classpath:clients-${spring.profiles.active}.properties")
})
public class VehicleApplication {
    public static void main(String[] args) {
        SpringApplication.run(VehicleApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(VehicleRepository repository) {

        return args -> {
            if (true) return;

            Vehicle bentley = Vehicle
                    .builder()
                    .year(2020)
                    .model("rolls-royce")
                    .build();
            Vehicle amg = Vehicle
                    .builder()
                    .year(2021)
                    .model("amg")
                    .build();

            repository.saveAll(
                    List.of(bentley, amg)
            );
        };
    };
}
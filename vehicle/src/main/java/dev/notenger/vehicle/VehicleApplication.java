package dev.notenger.vehicle;

import dev.notenger.vehicle.entity.Vehicle;
import dev.notenger.vehicle.repository.VehicleDao;
import dev.notenger.vehicle.repository.VehicleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

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
    CommandLineRunner commandLineRunner(VehicleRepository vehicleRepository) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                vehicleRepository.save(Vehicle.builder()
                        .vin("5363")
                        .make("eger")
                        .model("efew")
                        .year(425)
                        .groupName("43654")
                        .deviceId(3)
                        .averageSpeed(4363.)
                        .build());
            }
        };
    }

}
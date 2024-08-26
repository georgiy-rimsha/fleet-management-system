package dev.notenger.vehicle;

import dev.notenger.vehicle.entity.Vehicle;
import dev.notenger.vehicle.repository.VehicleDao;
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

}
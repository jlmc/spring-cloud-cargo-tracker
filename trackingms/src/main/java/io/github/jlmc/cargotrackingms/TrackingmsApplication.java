package io.github.jlmc.cargotrackingms;

import io.github.jlmc.cargotrackingms.infrastructure.brokers.rabbitmq.CargoEventInput;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;

@SpringBootApplication

@EnableBinding({CargoEventInput.class})
public class TrackingmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrackingmsApplication.class, args);
    }

}

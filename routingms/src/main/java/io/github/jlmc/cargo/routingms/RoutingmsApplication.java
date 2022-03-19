package io.github.jlmc.cargo.routingms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@org.springframework.cloud.client.discovery.EnableDiscoveryClient

@SpringBootApplication
public class RoutingmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(RoutingmsApplication.class, args);
    }

}

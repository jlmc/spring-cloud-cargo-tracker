package io.github.jlmc.cargo.bookingms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

// The `@EnableDiscoveryClient` activates the Netflix Eureka `DiscoveryClient` implementation.
@org.springframework.cloud.client.discovery.EnableDiscoveryClient

// The `@EnableFeignClients` enables FeignClient features
@org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
public class BookingmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookingmsApplication.class, args);
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.build();
    }

    @Bean
    @LoadBalanced
    public WebClient.Builder webClient() {
        return WebClient.builder();
    }

}

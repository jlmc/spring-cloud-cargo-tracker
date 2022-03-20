package io.github.jlmc.apigateway;

import io.netty.handler.logging.LogLevel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

@EnableEurekaClient

@SpringBootApplication
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    /**
     * This bean is only used allow us to log the request, to have a better trace and flow understand.
     */
    @Bean
    HttpClient httpClient() {
        return HttpClient.create()
                         .wiretap("LoggingFilter",
                                 LogLevel.INFO, AdvancedByteBufFormat.TEXTUAL);
    }
}

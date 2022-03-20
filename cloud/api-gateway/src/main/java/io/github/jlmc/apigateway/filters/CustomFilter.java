package io.github.jlmc.apigateway.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Set;

@Configuration
public class CustomFilter implements GlobalFilter {

    //@formatter:off
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomFilter.class);
    private static final Set<String> PATHS = Set.of("/booking", "/routing");
    //@formatter:on

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if (PATHS.stream().anyMatch(it -> request.getURI().toString().contains(it))) {
            LOGGER.debug(request.getURI().toString());
        }

        // here we are just logger the authentication header, but we could perform the authentication
        LOGGER.info("Pre-Filter Header Authorization: {}", request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION));


        // here we are just logging the response status, but we could perform some other tasks like auditing
        return chain.filter(exchange)
                    .then(Mono.fromRunnable(() -> {
                        ServerHttpResponse response = exchange.getResponse();

                        LOGGER.info("Post-Filter Status Code: {}", response.getRawStatusCode());

                    }));
    }

}

package io.github.jlmc.cargo.bookingms.application.internal.outboundservices.acl.gw;

import io.github.jlmc.cargo.bookingms.domain.model.valueobjects.RouteSpecification;
import io.github.jlmc.cargo.bookingms.shareddomain.TransitPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Note that it is necessary to add the org.springframework.boot:spring-boot-starter-webflux dependency
 */
@Component
public record CargoRoutingWithWebClient(WebClient.Builder webClientBuilder) {

    //@formatter:off
    private static final Logger LOGGER = LoggerFactory.getLogger(CargoRoutingWithRestTemplate.class);
    private static final String ROUTING = "http://localhost:8082/routing";
    private static final String ROUTING_MS = "http://routingms/routing";
    //@formatter:on

    public TransitPath findOptimalRoute(RouteSpecification routeSpecification) {

        LOGGER.info("Fetch Transit Path web flux");

        return webClientBuilder
                .baseUrl(ROUTING_MS)
                .build()
                .get()
                .uri(uriBuilder ->
                        uriBuilder.queryParam("origin", routeSpecification.getOrigin().getUnLocCode())
                                  .queryParam("destination", routeSpecification.getDestination().getUnLocCode())
                                  .queryParam("deadline", routeSpecification.getArrivalDeadline().toString())
                                  .build()).retrieve()
                .bodyToMono(TransitPath.class)
                .block();

        /*
        String uri =
                UriComponentsBuilder.fromUriString(ROUTING_MS)
                                    .queryParam("origin", routeSpecification.getOrigin().getUnLocCode())
                                    .queryParam("destination", routeSpecification.getDestination().getUnLocCode())
                                    .queryParam("deadline", routeSpecification.getArrivalDeadline().toString()).build()
                                    .toUriString();

        WebClient webClient = webClientBuilder.build();
        var s =
                webClient.get()
                         .uri(uri)
                         .retrieve()
                         .bodyToMono(TransitPath.class);

        TransitPath block = s.block();
        return block;
         */
    }
}

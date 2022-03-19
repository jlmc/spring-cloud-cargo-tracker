package io.github.jlmc.cargo.bookingms.application.internal.outboundservices.acl.gw;

import io.github.jlmc.cargo.bookingms.domain.model.valueobjects.RouteSpecification;
import io.github.jlmc.cargo.bookingms.shareddomain.TransitPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public record CargoRoutingWithRestTemplate(RestTemplate restTemplate) {

    //@formatter:off
    private static final Logger LOGGER = LoggerFactory.getLogger(CargoRoutingWithRestTemplate.class);
    private static final String ROUTING = "http://localhost:8082/routing";
    private static final String ROUTING_MS = "http://routingms/routing";
    //@formatter:on

    public TransitPath findOptimalRoute(RouteSpecification routeSpecification) {
        String uri =
                UriComponentsBuilder.fromUriString(ROUTING_MS)
                                    .queryParam("origin", routeSpecification.getOrigin().getUnLocCode())
                                    .queryParam("destination", routeSpecification.getDestination().getUnLocCode())
                                    .queryParam("deadline", routeSpecification.getArrivalDeadline().toString()).build()
                                    .toUriString();

        LOGGER.info("Fetch Transit Path: <{}>", uri);

        //restTemplate.getForObject("<<ROUTING_SERVICE_URL>>/cargorouting/",
        return this.restTemplate.getForObject(uri, TransitPath.class);
    }
}

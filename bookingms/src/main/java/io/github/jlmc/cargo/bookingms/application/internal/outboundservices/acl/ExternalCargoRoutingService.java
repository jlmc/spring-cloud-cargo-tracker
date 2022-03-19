package io.github.jlmc.cargo.bookingms.application.internal.outboundservices.acl;

import io.github.jlmc.cargo.bookingms.domain.model.valueobjects.CargoItinerary;
import io.github.jlmc.cargo.bookingms.domain.model.valueobjects.Leg;
import io.github.jlmc.cargo.bookingms.domain.model.valueobjects.RouteSpecification;
import io.github.jlmc.cargo.bookingms.shareddomain.TransitEdge;
import io.github.jlmc.cargo.bookingms.shareddomain.TransitPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class ExternalCargoRoutingService {

    //@formatter:off
    private static final Logger LOGGER = LoggerFactory.getLogger(ExternalCargoRoutingService.class);
    private static final String ROUTING = "http://localhost:8082/routing";
    private static final String ROUTING_MS = "http://routingms/routing";
    //@formatter:on

    private final RestTemplate restTemplate;

    public ExternalCargoRoutingService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public CargoItinerary fetchRouteForSpecification(RouteSpecification routeSpecification) {
        TransitPath transitPath = fetchTransitPath(routeSpecification);

        var legs =
                transitPath.getTransitEdges()
                           .stream()
                           .map(this::toLeg)
                           .toList();

        return CargoItinerary.withLegs(legs);
    }

    /**
     * Anti-corruption layer conversion method from the routing service's domain model (TransitEdges)
     * to the domain model recognized by the Booking Bounded Context (Legs)
     */
    private Leg toLeg(TransitEdge edge) {
        return new Leg(
                edge.getVoyageNumber(),
                edge.getFromUnLocode(),
                edge.getToUnLocode(),
                edge.getFromDate(),
                edge.getToDate());
    }

    private TransitPath fetchTransitPath(RouteSpecification routeSpecification) {

        String uri =
                UriComponentsBuilder.fromUriString(ROUTING_MS)
                                    .queryParam("origin", routeSpecification.getOrigin().getUnLocCode())
                                    .queryParam("destination", routeSpecification.getDestination().getUnLocCode())
                                    .queryParam("deadline", routeSpecification.getArrivalDeadline().toString()).build()
                                    .toUriString();

        LOGGER.info("Fetch Transit Path: <{}>", uri);

        TransitPath transitPath =
                //restTemplate.getForObject("<<ROUTING_SERVICE_URL>>/cargorouting/",
                this.restTemplate.getForObject(uri, TransitPath.class);

        return transitPath;
    }
}

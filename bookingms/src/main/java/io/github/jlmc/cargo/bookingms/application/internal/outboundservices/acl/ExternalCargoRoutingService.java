package io.github.jlmc.cargo.bookingms.application.internal.outboundservices.acl;

import io.github.jlmc.cargo.bookingms.domain.model.valueobjects.CargoItinerary;
import io.github.jlmc.cargo.bookingms.domain.model.valueobjects.Leg;
import io.github.jlmc.cargo.bookingms.domain.model.valueobjects.RouteSpecification;
import io.github.jlmc.cargo.bookingms.shareddomain.TransitEdge;
import io.github.jlmc.cargo.bookingms.shareddomain.TransitPath;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class ExternalCargoRoutingService {
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
        RestTemplate restTemplate = new RestTemplate();

        String uri =
                UriComponentsBuilder.fromUriString("http://localhost:8082/routing")
                                       .queryParam("origin", routeSpecification.getOrigin().getUnLocCode())
                                       .queryParam("destination", routeSpecification.getDestination().getUnLocCode())
                                       .queryParam("deadline", routeSpecification.getArrivalDeadline().toString()).build()
                                       .toUriString();

        TransitPath transitPath =
                //restTemplate.getForObject("<<ROUTING_SERVICE_URL>>/cargorouting/",
                restTemplate.getForObject(uri, TransitPath.class);

        return transitPath;
    }
}

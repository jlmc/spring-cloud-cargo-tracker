package io.github.jlmc.cargo.bookingms.application.internal.outboundservices.acl;

import io.github.jlmc.cargo.bookingms.application.internal.outboundservices.acl.gw.CargoRoutingOpenfeign;
import io.github.jlmc.cargo.bookingms.application.internal.outboundservices.acl.gw.CargoRoutingWithRestTemplate;
import io.github.jlmc.cargo.bookingms.application.internal.outboundservices.acl.gw.CargoRoutingWithWebClient;
import io.github.jlmc.cargo.bookingms.domain.model.valueobjects.CargoItinerary;
import io.github.jlmc.cargo.bookingms.domain.model.valueobjects.Leg;
import io.github.jlmc.cargo.bookingms.domain.model.valueobjects.RouteSpecification;
import io.github.jlmc.cargo.bookingms.shareddomain.TransitEdge;
import io.github.jlmc.cargo.bookingms.shareddomain.TransitPath;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ExternalCargoRoutingService {

    //@formatter:off
    private static final Logger LOGGER = LoggerFactory.getLogger(ExternalCargoRoutingService.class);
    //@formatter:on

    private final CargoRoutingWithRestTemplate restClint;
    private final CargoRoutingWithWebClient webClient;
    private final CargoRoutingOpenfeign cargoRoutingOpenfeign;

    public ExternalCargoRoutingService(CargoRoutingWithRestTemplate restClint,
                                       CargoRoutingWithWebClient webClient,
                                       CargoRoutingOpenfeign cargoRoutingOpenfeign) {
        this.restClint = restClint;
        this.webClient = webClient;
        this.cargoRoutingOpenfeign = cargoRoutingOpenfeign;
    }

    @CircuitBreaker(
            name = "fetchRouteForSpecification",
            fallbackMethod = "fallbackFetchRouteForSpecification"
    )
    public CargoItinerary fetchRouteForSpecification(RouteSpecification routeSpecification) {
        //TransitPath transitPath = restClint.findOptimalRoute(routeSpecification);
        //TransitPath transitPath = webClient.findOptimalRoute(routeSpecification);


        TransitPath transitPath =
                cargoRoutingOpenfeign.findOptimalRoute(
                routeSpecification.getOrigin().getUnLocCode(),
                        routeSpecification.getDestination().getUnLocCode(),
                        routeSpecification.getArrivalDeadline().toString()
                        );

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

    public CargoItinerary fallbackFetchRouteForSpecification(RouteSpecification routeSpecification, Throwable throwable) {
        LOGGER.error("Fallback Fetch Route For Specification <{}>", routeSpecification, throwable);

        return new CargoItinerary();
    }

}

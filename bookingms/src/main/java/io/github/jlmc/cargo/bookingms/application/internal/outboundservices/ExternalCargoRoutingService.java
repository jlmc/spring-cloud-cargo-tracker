package io.github.jlmc.cargo.bookingms.application.internal.outboundservices;

import io.github.jlmc.cargo.bookingms.domain.model.valueobjects.CargoItinerary;
import io.github.jlmc.cargo.bookingms.domain.model.valueobjects.Leg;
import io.github.jlmc.cargo.bookingms.domain.model.valueobjects.RouteSpecification;
import io.github.jlmc.cargo.bookingms.shareddomain.TransitEdge;
import io.github.jlmc.cargo.bookingms.shareddomain.TransitPath;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExternalCargoRoutingService {
    public CargoItinerary fetchRouteForSpecification(RouteSpecification routeSpecification) {
        TransitPath transitPath = extracted();

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

    private TransitPath extracted() {
        TransitPath transitPath = new TransitPath();
        List<TransitEdge> transitEdges = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            TransitEdge transitEdge = new TransitEdge();
            transitEdge.setVoyageNumber("V11");
            transitEdge.setFromUnLocode("CHK");
            transitEdge.setFromDate(Instant.now().plus(30L, ChronoUnit.DAYS));
            transitEdge.setToDate(Instant.now().plus(60L, ChronoUnit.DAYS));
            transitEdge.setToUnLocode("NYC");
            transitEdges.add(transitEdge);
        }
        transitPath.setTransitEdges(transitEdges);

        return transitPath;
    }
}

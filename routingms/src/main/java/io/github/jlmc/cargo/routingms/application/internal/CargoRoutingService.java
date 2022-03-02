package io.github.jlmc.cargo.routingms.application.internal;

import io.github.jlmc.cargo.routingms.shareddomain.TransitEdge;
import io.github.jlmc.cargo.routingms.shareddomain.TransitPath;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.IntStream;

@Service
public class CargoRoutingService {

    public TransitPath findOptimalRoute(String originUnLocode, String destinationUnLocode, String deadline) {
        var transitEdges =
                IntStream.rangeClosed(1, 4)
                         .mapToObj(it -> {
                             TransitEdge transitEdge = new TransitEdge();
                             transitEdge.setVoyageNumber("V11");
                             transitEdge.setFromUnLocode("CHK");
                             transitEdge.setFromDate(Instant.now().plus(30L, ChronoUnit.DAYS));
                             transitEdge.setToDate(Instant.now().plus(60L, ChronoUnit.DAYS));
                             transitEdge.setToUnLocode("NYC");
                             return transitEdge;
                         }).toList();

        TransitPath transitPath = new TransitPath();
        transitPath.setTransitEdges(transitEdges);
        return transitPath;
    }
}

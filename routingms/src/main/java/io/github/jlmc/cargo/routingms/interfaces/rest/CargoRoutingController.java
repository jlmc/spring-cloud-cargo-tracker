package io.github.jlmc.cargo.routingms.interfaces.rest;

import io.github.jlmc.cargo.routingms.application.internal.CargoRoutingService;
import io.github.jlmc.cargo.routingms.shareddomain.TransitPath;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor

@RestController
@RequestMapping(path = "routing",
        produces = MediaType.APPLICATION_JSON_VALUE)
public class CargoRoutingController {

    private final CargoRoutingService cargoRoutingService; // Application Service Dependency

    @GetMapping
    public TransitPath findOptimalRoute(
            @RequestParam(value = "origin", required = false) String originUnLocode,
            @RequestParam(value = "destination", required = false) String destinationUnLocode,
            @RequestParam(value = "deadline", required = false) String deadline) {

        log.info("find Optimal Route origin <{}>, destination <{}>, deadline <{}>", originUnLocode, destinationUnLocode, deadline);

        return cargoRoutingService.findOptimalRoute(originUnLocode, destinationUnLocode, deadline);
    }
}

package io.github.jlmc.cargo.routingms.interfaces.rest;

import io.github.jlmc.cargo.routingms.application.internal.CargoRoutingService;
import io.github.jlmc.cargo.routingms.shareddomain.TransitPath;
import io.github.jlmc.cargo.routingms.util.RequestHeadersContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Optional;

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
            @RequestParam(value = "deadline", required = false) String deadline,
            @RequestHeader HttpHeaders headers) {

        RequestHeadersContext.set(headers);
        String authentication = RequestHeadersContext.authentication().orElse("unknown");
        String xSys = RequestHeadersContext.xsys().orElse("-");

        log.info("[{} - {}] Find Optimal Route origin <{}>, destination <{}>, deadline <{}>", authentication, xSys, originUnLocode, destinationUnLocode, deadline);

        return cargoRoutingService.findOptimalRoute(originUnLocode, destinationUnLocode, deadline);
    }

    private String headerValue(HttpHeaders headers, String headerName, String defaultValue) {
        return Optional.ofNullable(
                               headers.get(headerName))
                       .stream()
                       .flatMap(Collection::stream)
                       .findFirst()
                       .orElse(defaultValue);
    }
}

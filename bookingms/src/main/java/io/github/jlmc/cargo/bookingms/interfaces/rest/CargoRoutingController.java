package io.github.jlmc.cargo.bookingms.interfaces.rest;

import io.github.jlmc.cargo.bookingms.infrastructure.http.RequestHeadersContext;
import io.github.jlmc.cargo.bookingms.application.internal.commandservices.CargoBookingCommandService;
import io.github.jlmc.cargo.bookingms.interfaces.rest.dto.RouteCargoResource;
import io.github.jlmc.cargo.bookingms.interfaces.rest.transform.RouteCargoCommandDTOAssembler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@AllArgsConstructor
@Slf4j

@Validated
@RestController
@RequestMapping(
        path = "booking/{bookingId}/route",
        produces = MediaType.APPLICATION_JSON_VALUE)
public class CargoRoutingController {

    private final CargoBookingCommandService cargoBookingCommandService;

    @PostMapping
    public ResponseEntity<String> routeCargo(@Valid @PathVariable("bookingId") RouteCargoResource routeCargoResource, @RequestHeader HttpHeaders headers) {
        RequestHeadersContext.set(headers);
        String authentication = RequestHeadersContext.authentication().orElse( "unknown");
        String xSys = RequestHeadersContext.xsys().orElse( "-");

        log.info("[{} - {}] Cargo Routing Id <{}>", authentication, xSys, routeCargoResource);

        cargoBookingCommandService.assignRouteToCargo(
                RouteCargoCommandDTOAssembler.toCommandFromDTO(routeCargoResource));

        return ResponseEntity.ok("Cargo Routed");
    }
}

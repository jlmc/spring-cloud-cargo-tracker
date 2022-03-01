package io.github.jlmc.cargo.bookingms.interfaces.rest;


import io.github.jlmc.cargo.bookingms.application.internal.commandservices.CargoBookingCommandService;
import io.github.jlmc.cargo.bookingms.application.internal.queryservices.CargoBookingQueryService;
import io.github.jlmc.cargo.bookingms.domain.model.aggregates.BookingId;
import io.github.jlmc.cargo.bookingms.domain.model.aggregates.Cargo;
import io.github.jlmc.cargo.bookingms.interfaces.rest.transform.BookCargoCommandDTOAssembler;
import io.github.jlmc.cargo.bookingms.interfaces.rest.dto.BookCargoResource;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@AllArgsConstructor

@Validated
@RestController
@RequestMapping(
        path = "/booking",
        produces = MediaType.APPLICATION_JSON_VALUE)
public class CargoBookingController {

    private final CargoBookingCommandService cargoBookingCommandService;
    private final CargoBookingQueryService cargoBookingQueryService;

    @PostMapping
    public BookingId bookCargo(@RequestBody BookCargoResource bookCargoResource) {
        log.info("Cargo Book <{}>", bookCargoResource.getBookingAmount());

        return cargoBookingCommandService.bookCargo(
                BookCargoCommandDTOAssembler.toCommandFromDTO(bookCargoResource));
    }

    @GetMapping(path = "/{bookingId}")
    public ResponseEntity<Cargo> findByBookingId(@PathVariable("bookingId") String bookingId){
        log.info("Get Cargo Book <{}>", bookingId);
        return cargoBookingQueryService.find(bookingId)
                                       .map(ResponseEntity::ok)
                                       .orElseGet(() -> ResponseEntity.notFound().build());
    }
}

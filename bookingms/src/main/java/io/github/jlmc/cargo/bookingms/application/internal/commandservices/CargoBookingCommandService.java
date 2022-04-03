package io.github.jlmc.cargo.bookingms.application.internal.commandservices;

import io.github.jlmc.cargo.bookingms.application.internal.outboundservices.acl.BookingIdGeneratorService;
import io.github.jlmc.cargo.bookingms.application.internal.outboundservices.acl.ExternalCargoRoutingService;
import io.github.jlmc.cargo.bookingms.domain.model.aggregates.BookingId;
import io.github.jlmc.cargo.bookingms.domain.model.aggregates.Cargo;
import io.github.jlmc.cargo.bookingms.domain.model.commands.BookCargoCommand;
import io.github.jlmc.cargo.bookingms.domain.model.commands.RouteCargoCommand;
import io.github.jlmc.cargo.bookingms.domain.model.valueobjects.CargoItinerary;
import io.github.jlmc.cargo.bookingms.infrastructure.repositories.CargoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@AllArgsConstructor

@Service
@Transactional(readOnly = true)
public class CargoBookingCommandService {

    private final BookingIdGeneratorService bookingIdGeneratorService;
    private final ExternalCargoRoutingService externalCargoRoutingService;
    private final CargoRepository cargoRepository;

    @Transactional(isolation = Isolation.SERIALIZABLE, readOnly = false)
    public BookingId bookCargo(BookCargoCommand bookCargoCommand) {
        String random = bookingIdGeneratorService.generate();

        log.info("Random is : <{}>", random);

        bookCargoCommand.setBookingId(random);

        Cargo cargo = new Cargo(bookCargoCommand);

        cargoRepository.save(cargo);

        return BookingId.of(random);
    }

    /**
     * Service Command method to assign a route to a Cargo
     * @param routeCargoCommand
     */

    @Transactional(isolation = Isolation.SERIALIZABLE, readOnly = false)
    public void assignRouteToCargo(RouteCargoCommand routeCargoCommand){
        Cargo bookedCargo =
                cargoRepository.findByBookingId(BookingId.of(routeCargoCommand.bookingId()))
                               .orElse(null);

        if (bookedCargo == null) return;

        CargoItinerary cargoItinerary = externalCargoRoutingService.fetchRouteForSpecification(bookedCargo.getRouteSpecification());

        bookedCargo.assignToRoute(cargoItinerary);
        cargoRepository.save(bookedCargo);
    }
}

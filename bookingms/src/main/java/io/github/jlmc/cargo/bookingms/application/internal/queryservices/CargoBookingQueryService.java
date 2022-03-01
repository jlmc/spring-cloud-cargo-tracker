package io.github.jlmc.cargo.bookingms.application.internal.queryservices;

import io.github.jlmc.cargo.bookingms.domain.model.aggregates.BookingId;
import io.github.jlmc.cargo.bookingms.domain.model.aggregates.Cargo;
import io.github.jlmc.cargo.bookingms.interfaces.repositories.CargoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Application Service which caters to all queries related to the Booking Bounded Context
 */

@AllArgsConstructor

@Service
public class CargoBookingQueryService {

    private final CargoRepository cargoRepository;

    public Optional<Cargo> find(String bookingId) {
        return cargoRepository.findByBookingId(BookingId.of(bookingId));
    }
}

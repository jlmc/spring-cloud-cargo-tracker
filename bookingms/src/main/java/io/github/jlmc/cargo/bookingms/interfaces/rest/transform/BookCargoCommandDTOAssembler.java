package io.github.jlmc.cargo.bookingms.interfaces.rest.transform;

import io.github.jlmc.cargo.bookingms.domain.model.commands.BookCargoCommand;
import io.github.jlmc.cargo.bookingms.interfaces.rest.dto.BookCargoResource;

/**
 * Assembler class to convert the Book Cargo Resource Data to the Book Cargo Model
 */
public class BookCargoCommandDTOAssembler {
    public static BookCargoCommand toCommandFromDTO(BookCargoResource bookCargoResource) {
        return BookCargoCommand.createBookCargoCommand(
                bookCargoResource.getBookingAmount(),
                bookCargoResource.getOriginLocation(),
                bookCargoResource.getDestLocation(),
                bookCargoResource.getDestArrivalDeadline());
    }
}

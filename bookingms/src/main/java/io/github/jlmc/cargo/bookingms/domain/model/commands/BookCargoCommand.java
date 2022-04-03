package io.github.jlmc.cargo.bookingms.domain.model.commands;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@NoArgsConstructor

@Data
public class BookCargoCommand {
    private String bookingId;
    private int bookingAmount;
    private String originLocation;
    private String destLocation;
    private Instant destArrivalDeadline;

    private BookCargoCommand(int bookingAmount,
                             String originLocation,
                             String destLocation,
                             Instant destArrivalDeadline) {
        this.bookingAmount = bookingAmount;
        this.originLocation = originLocation;
        this.destLocation = destLocation;
        this.destArrivalDeadline = destArrivalDeadline;
    }

    public static BookCargoCommand createBookCargoCommand(int bookingAmount, String originLocation, String destLocation, Instant destArrivalDeadline) {
        return new BookCargoCommand(bookingAmount, originLocation, destLocation, destArrivalDeadline);
    }
}

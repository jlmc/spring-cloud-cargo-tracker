package io.github.jlmc.cargo.bookingms.domain.model.commands;

import lombok.Data;

@Data
public class RouteCargoCommand {
    private String bookingId;

    public RouteCargoCommand(String bookingId) {
        this.bookingId = bookingId;
    }
}

package io.github.jlmc.cargotrackingms.shareddomain.events;

import lombok.Data;

/**
 * Event Data for the Cargo Booked Event
 */
@Data
public class CargoRoutedEventData {

    private String bookingId;

}

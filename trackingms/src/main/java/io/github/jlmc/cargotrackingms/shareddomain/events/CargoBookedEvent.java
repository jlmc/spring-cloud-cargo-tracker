package io.github.jlmc.cargotrackingms.shareddomain.events;

import lombok.Data;

/**
 * Event Class for the Cargo Booked Event. Wraps up the Cargo Booked Event Data
 */

@Data
public class CargoBookedEvent {
    CargoBookedEventData cargoBookedEventData;
}

package io.github.jlmc.cargotrackingms.shareddomain.events;

import lombok.Data;

/**
 * Event Class for the Cargo Routed Event. Wraps up the Cargo Routed Event Data
 */
@Data
public class CargoRoutedEvent {
    CargoRoutedEventData cargoRoutedEventData;
}

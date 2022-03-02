package io.github.jlmc.cargotrackingms.infrastructure.brokers.rabbitmq;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @see org.springframework.cloud.stream.messaging.Sink
 */
public interface CargoEventInput {

    String ROUTINGS = "routings";

    String BOOKINGS = "bookings";

    @Input(value = ROUTINGS)
    SubscribableChannel routings();

    @Input(value = BOOKINGS)
    SubscribableChannel bookings();
}

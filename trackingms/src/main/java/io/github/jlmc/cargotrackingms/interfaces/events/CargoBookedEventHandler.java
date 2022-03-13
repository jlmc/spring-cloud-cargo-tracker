package io.github.jlmc.cargotrackingms.interfaces.events;

import io.github.jlmc.cargotrackingms.infrastructure.brokers.rabbitmq.CargoEventInput;
import io.github.jlmc.cargotrackingms.shareddomain.events.CargoBookedEvent;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

/**
 * Event Handler for the Cargo Booked Event Handler that the Tracking Bounded Context is interested in
 */
@Service
//@EnableBinding(Sink.class)
public class CargoBookedEventHandler {

    @StreamListener(target = CargoEventInput.BOOKINGS)
    public void receiveEvent(CargoBookedEvent cargoBookedEvent) {
        //Process the Event
        System.out.println("Booked: ---> " + cargoBookedEvent);
    }
}

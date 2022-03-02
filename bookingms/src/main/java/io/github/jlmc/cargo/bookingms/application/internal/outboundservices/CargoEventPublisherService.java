package io.github.jlmc.cargo.bookingms.application.internal.outboundservices;

import io.github.jlmc.cargo.bookingms.infrastructure.brokers.rabbitmq.CargoEventSource;
import org.springframework.stereotype.Service;

@Service
//@EnableBinding(CargoEventSource.class)
public class CargoEventPublisherService {

    CargoEventSource cargoEventSource;

    /*
    public CargoEventPublisherService(CargoEventSource cargoEventSource){
        this.cargoEventSource = cargoEventSource;
    }

    @TransactionalEventListener
    public void handleCargoBookedEvent(CargoBookedEvent cargoBookedEvent){
        cargoEventSource.cargoBooking().send(MessageBuilder.withPayload(cargoBookedEvent).build());
    }

    @TransactionalEventListener
    public void handleCargoRoutedEvent(CargoRoutedEvent cargoRoutedEvent){
        cargoEventSource.cargoRouting().send(MessageBuilder.withPayload(cargoRoutedEvent).build());
    }

     */
}

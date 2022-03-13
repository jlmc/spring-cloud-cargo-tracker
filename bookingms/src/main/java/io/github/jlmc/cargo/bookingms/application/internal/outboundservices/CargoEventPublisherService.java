package io.github.jlmc.cargo.bookingms.application.internal.outboundservices;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.jlmc.cargo.bookingms.shareddomain.events.CargoBookedEvent;
import io.github.jlmc.cargo.bookingms.shareddomain.events.CargoRoutedEvent;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

import java.nio.charset.StandardCharsets;

@Service
public class CargoEventPublisherService {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public CargoEventPublisherService(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    @TransactionalEventListener
    public void handleCargoBookedEvent(CargoBookedEvent cargoBookedEvent){

        var message = MessageBuilder
                .withBody(toPayload(cargoBookedEvent).getBytes(StandardCharsets.UTF_8))
                .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                .build();

        rabbitTemplate.convertAndSend("cargoBookingChannel", "#", message);
    }

    @TransactionalEventListener
    public void handleCargoRoutedEvent(CargoRoutedEvent cargoRoutedEvent){
        var message = MessageBuilder
                .withBody(toPayload(cargoRoutedEvent).getBytes(StandardCharsets.UTF_8))
                .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                .build();
        rabbitTemplate.convertAndSend("cargoRoutingChannel", "#", message);
    }

//    private CargoEventSource cargoEventSource;
//
//
//    public CargoEventPublisherService(CargoEventSource cargoEventSource){
//        this.cargoEventSource = cargoEventSource;
//    }
//
//    @TransactionalEventListener
//    public void handleCargoBookedEvent(CargoBookedEvent cargoBookedEvent){
//        cargoEventSource.cargoBooking().send(MessageBuilder.withPayload(cargoBookedEvent).build());
//    }
//
//    @TransactionalEventListener
//    public void handleCargoRoutedEvent(CargoRoutedEvent cargoRoutedEvent){
//        cargoEventSource.cargoRouting().send(MessageBuilder.withPayload(cargoRoutedEvent).build());
//    }

    private String toPayload(Object obj) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

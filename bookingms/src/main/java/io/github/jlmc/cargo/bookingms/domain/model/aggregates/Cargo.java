package io.github.jlmc.cargo.bookingms.domain.model.aggregates;

import io.github.jlmc.cargo.bookingms.domain.model.commands.BookCargoCommand;
import io.github.jlmc.cargo.bookingms.domain.model.entities.Location;
import io.github.jlmc.cargo.bookingms.domain.model.valueobjects.CargoItinerary;
import io.github.jlmc.cargo.bookingms.domain.model.valueobjects.RouteSpecification;
import io.github.jlmc.cargo.bookingms.shareddomain.events.CargoBookedEvent;
import io.github.jlmc.cargo.bookingms.shareddomain.events.CargoBookedEventData;
import io.github.jlmc.cargo.bookingms.shareddomain.events.CargoRoutedEvent;
import io.github.jlmc.cargo.bookingms.shareddomain.events.CargoRoutedEventData;
import lombok.Getter;
import org.springframework.data.domain.AbstractAggregateRoot;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter

@Entity
public class Cargo extends AbstractAggregateRoot<Cargo> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private BookingId bookingId; // Aggregate Identifier

    @Embedded
    private Location origin; //Origin Location of the Cargo

    @Embedded
    private RouteSpecification routeSpecification; //Route Specification of the Cargo

    @Embedded
    private CargoItinerary itinerary; //Itinerary Assigned to the Cargo

    public Cargo() {
        // Because JPA, So Why JPA Why???
    }

    public Cargo(BookCargoCommand bookCargoCommand) {
        this.bookingId = BookingId.of(bookCargoCommand.getBookingId());

        this.routeSpecification = new RouteSpecification(
                Location.of(bookCargoCommand.getOriginLocation()),
                Location.of(bookCargoCommand.getDestLocation()),
                bookCargoCommand.getDestArrivalDeadline()
        );

        this.origin = Location.of(bookCargoCommand.getOriginLocation());
        this.itinerary = CargoItinerary.emptyItinerary(); //Empty Itinerary since the Cargo has not been routed yet

        //Add this domain event which needs to be fired when the new cargo is saved
        addDomainEvent(new
                CargoBookedEvent(
                new CargoBookedEventData(bookingId.getBookingId())));
    }

    public void assignToRoute(CargoItinerary itinerary) {
        this.itinerary.setLegs(itinerary.getLegs());
        //Add this domain event which needs to be fired when the new cargo is saved
        addDomainEvent(new
                CargoRoutedEvent(
                new CargoRoutedEventData(bookingId.getBookingId())));
    }

    /**
     * Method to register the event
     */
    public void addDomainEvent(Object event) {
        registerEvent(event);
    }

}

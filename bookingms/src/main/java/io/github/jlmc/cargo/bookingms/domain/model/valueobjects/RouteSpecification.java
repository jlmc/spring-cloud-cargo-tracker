package io.github.jlmc.cargo.bookingms.domain.model.valueobjects;

import io.github.jlmc.cargo.bookingms.domain.model.entities.Location;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

@Getter
@ToString
@AllArgsConstructor

@Embeddable
public class RouteSpecification implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Embedded
    @AttributeOverride(name = "unLocCode", column = @Column(name = "spec_origin_id"))
    private Location origin;

    @Embedded
    @AttributeOverride(name = "unLocCode", column = @Column(name = "spec_destination_id"))
    private Location destination;

    @NotNull
    @Column(name = "spec_arrival_deadline")
    private Instant arrivalDeadline;

    public RouteSpecification() {
        // Because of JPA. So why JPA why?
    }
}

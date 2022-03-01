package io.github.jlmc.cargo.bookingms.domain.model.valueobjects;

import io.github.jlmc.cargo.bookingms.domain.model.entities.Location;
import lombok.NoArgsConstructor;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@NoArgsConstructor

@Entity
public class Leg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Voyage voyage;

    @Embedded
    @AttributeOverride(name = "unLocCode", column = @Column(name = "load_location_id"))
    private Location loadLocation;

    @Embedded
    @AttributeOverride(name = "unLocCode", column = @Column(name = "unload_location_id"))
    private Location unloadLocation;

    @NotNull
    @Column(name = "load_time")
    private Instant loadTime;

    @NotNull
    @Column(name = "unload_time")
    private Instant unloadTime;

    public Leg(String voyageNumber, String loadLocationCode, String unloadLocationCode, Instant loadTime, Instant unloadTime) {
        this.voyage = Voyage.of(voyageNumber);
        this.loadLocation = Location.of(loadLocationCode);
        this.unloadLocation = Location.of(unloadLocationCode);
        this.loadTime = loadTime;
        this.unloadTime = unloadTime;
    }
}

package io.github.jlmc.cargo.bookingms.domain.model.valueobjects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.jlmc.cargo.bookingms.domain.model.aggregates.Cargo;
import lombok.NonNull;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Embeddable
public class CargoItinerary implements Serializable {

    @ToString.Exclude
    @JsonIgnore
    @OneToMany(
            orphanRemoval = true, // when the orphanRemoval is set with true the CascadeType.REMOVE is redundant,
            cascade = CascadeType.ALL
    )
    @JoinColumn(name = "cargo_id", nullable = false, updatable = false)
    @OrderBy("loadTime")
    private List<Leg> legs = new ArrayList<>();

    public CargoItinerary() {
        // Nothing to initialize.
    }

    private CargoItinerary(List<Leg> legs) {
        this.legs = legs;
    }

    public static CargoItinerary withLegs(@NonNull List<Leg> legs) {
        Objects.requireNonNull(legs);
        ArrayList<Leg> objects = new ArrayList<>(legs);
        return new CargoItinerary(objects);
    }

    public static CargoItinerary emptyItinerary() {
        return new CargoItinerary();
    }

    @JsonIgnore
    public List<Leg> getLegs() {
        return List.copyOf(this.legs);
    }

    public void setLegs(List<Leg> legs) {
        this.legs.clear();
        this.legs.addAll(legs);
    }
}

package io.github.jlmc.cargo.bookingms.domain.model.aggregates;

import lombok.Getter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Getter
@ToString

@Embeddable
public class BookingId implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = "booking_id", unique = true)
    private String bookingId;

    public BookingId() {
        // Because of JPA. So why JPA why?
    }

    private BookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public static BookingId of(String bookingId) {
        return new BookingId(bookingId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookingId bookingId1)) return false;
        return Objects.equals(bookingId, bookingId1.bookingId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookingId);
    }
}

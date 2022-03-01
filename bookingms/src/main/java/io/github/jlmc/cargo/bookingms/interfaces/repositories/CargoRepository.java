package io.github.jlmc.cargo.bookingms.interfaces.repositories;

import io.github.jlmc.cargo.bookingms.domain.model.aggregates.BookingId;
import io.github.jlmc.cargo.bookingms.domain.model.aggregates.Cargo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CargoRepository extends JpaRepository<Cargo, Long> {

    @Query("select c from Cargo c where c.bookingId = :bookingId")
    Optional<Cargo> findByBookingId(@Param("bookingId") BookingId bookingId);

    @Query("select c.bookingId from Cargo c")
    List<BookingId> findAllBookingIds();
}

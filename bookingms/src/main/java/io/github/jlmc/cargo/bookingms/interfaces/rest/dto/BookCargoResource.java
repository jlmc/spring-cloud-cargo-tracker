package io.github.jlmc.cargo.bookingms.interfaces.rest.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.Instant;

@Data
public class BookCargoResource {
    @Positive
    private int bookingAmount;
    @NotBlank
    private String originLocation;
    @NotBlank
    private String destLocation;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant destArrivalDeadline;
}

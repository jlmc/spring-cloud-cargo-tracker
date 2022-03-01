package io.github.jlmc.cargo.bookingms.interfaces.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor(staticName = "valueOf")
@NoArgsConstructor
@Data

public class RouteCargoResource {
    @NotBlank
    private String bookingId;
}

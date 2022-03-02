package io.github.jlmc.cargo.routingms.shareddomain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

/**
 * Represents an edge in a path through a graph, describing the route of a cargo.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransitEdge implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String voyageNumber;
    private String fromUnLocode;
    private String toUnLocode;
    private Instant fromDate;
    private Instant toDate;
}

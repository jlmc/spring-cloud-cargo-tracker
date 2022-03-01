package io.github.jlmc.cargo.bookingms.shareddomain;

import lombok.Data;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

/**
 * Represents an edge in a path through a graph, describing the route of a
 * cargo.
 */
@Data
public class TransitEdge implements Serializable {

    private String voyageNumber;
    private String fromUnLocode;
    private String toUnLocode;
    private Instant fromDate;
    private Instant toDate;

    public TransitEdge() {
        // Nothing to do.
    }

    public TransitEdge(String voyageNumber,
                       String fromUnLocode,
                       String toUnLocode,
                       Instant fromDate,
                       Instant toDate) {
        this.voyageNumber = voyageNumber;
        this.fromUnLocode = fromUnLocode;
        this.toUnLocode = toUnLocode;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }
}

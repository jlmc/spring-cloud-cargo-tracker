package io.github.jlmc.cargo.bookingms.shareddomain;


import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class TransitPath implements Serializable {

    private List<TransitEdge> transitEdges;

    public TransitPath() {
        this.transitEdges = new ArrayList<>();
    }

    public TransitPath(List<TransitEdge> transitEdges) {
        this.transitEdges = transitEdges;
    }
}

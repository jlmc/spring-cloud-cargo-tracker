package io.github.jlmc.cargo.routingms.shareddomain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransitPath implements Serializable {

    private List<TransitEdge> transitEdges;
}

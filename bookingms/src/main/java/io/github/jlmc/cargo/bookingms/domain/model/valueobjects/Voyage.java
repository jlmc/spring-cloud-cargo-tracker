package io.github.jlmc.cargo.bookingms.domain.model.valueobjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")

@Embeddable
public class Voyage implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = "voyage_id", insertable = false, updatable = false)
    private String voyageNumber;
}

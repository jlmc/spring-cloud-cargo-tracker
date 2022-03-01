package io.github.jlmc.cargo.bookingms.domain.model.entities;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(staticName = "of")

@Embeddable
public class Location implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = "origin_id", insertable = true, updatable = false)
    private String unLocCode;
}

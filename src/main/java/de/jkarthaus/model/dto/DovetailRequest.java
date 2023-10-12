package de.jkarthaus.model.dto;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import lombok.Data;
@Introspected
@Data
@Serdeable
public class DovetailRequest {
    Double width;
    Double thickness;
}

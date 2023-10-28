package de.jkarthaus.model.dto;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Introspected
@Serdeable
@AllArgsConstructor
public class DovetailResponse {
    String OutsideDovetailGCode;
    String InsideDovetailGCode;
}

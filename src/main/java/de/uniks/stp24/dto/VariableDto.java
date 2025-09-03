package de.uniks.stp24.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record VariableDto(
        String variable,
        double initial,
        List<EffectSourceDto> sources,
        @JsonProperty("final")
        double finalValue
) {
}
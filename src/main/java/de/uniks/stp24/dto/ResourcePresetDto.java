package de.uniks.stp24.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ResourcePresetDto(
        int starting,
        @JsonProperty("credit_value")
        Integer creditValue
) {
}
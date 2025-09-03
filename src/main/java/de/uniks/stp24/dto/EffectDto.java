package de.uniks.stp24.dto;

public record EffectDto(
        String variable,
        Integer base,
        Double multiplier,
        Integer bonus
) {
}

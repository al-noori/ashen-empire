package de.uniks.stp24.dto;

public record TraitsDto(
        String id,
        EffectDto[] effects,
        int cost,
        String[] conflicts
) {
}

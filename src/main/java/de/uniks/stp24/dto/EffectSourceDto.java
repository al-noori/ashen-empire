package de.uniks.stp24.dto;

import java.util.List;

public record EffectSourceDto(
        String id,
        List<EffectDto> effects
) {
}

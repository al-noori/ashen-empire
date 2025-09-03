package de.uniks.stp24.dto;

import java.util.List;

public record EffectSource(
        String id,
        List<EffectDto> effects
) {
}

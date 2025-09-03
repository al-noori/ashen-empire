package de.uniks.stp24.dto;

import java.util.List;

public record TechnologyDto(
        String id,
        List<EffectDto> effects,
        List<String> tags,
        Integer cost,
        List<String> requires,
        List<String> precedes
) {
}

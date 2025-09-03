package de.uniks.stp24.dto;

public record DistrictsDto(
        int city,
        int energy,
        int mining,
        int agriculture,
        int industry,
        int research_site,
        int ancient_foundry,
        int ancient_factory,
        int ancient_refinery
) {
}

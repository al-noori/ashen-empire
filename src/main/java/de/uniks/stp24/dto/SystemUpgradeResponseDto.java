package de.uniks.stp24.dto;

public record SystemUpgradeResponseDto(
        SystemUpgradeDto unexplored,
        SystemUpgradeDto explored,
        SystemUpgradeDto colonized,
        SystemUpgradeDto upgraded,
        SystemUpgradeDto developed
) {
}

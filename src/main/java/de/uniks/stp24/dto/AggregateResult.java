package de.uniks.stp24.dto;

public record AggregateResult(
        int total,
        AggregateItem[] items
) {
}

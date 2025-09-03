package de.uniks.stp24.dto;

public record AggregateItem(
        String variable,
        int count,
        int subtotal
) {
}

package de.uniks.stp24.dto;

public record ResourceDto(
        int credits,
        int population,
        int energy,
        int minerals,
        int food,
        int fuel,
        int research,
        int alloys,
        int consumer_goods
) {
}
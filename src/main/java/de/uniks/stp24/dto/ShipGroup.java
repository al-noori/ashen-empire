package de.uniks.stp24.dto;


import de.uniks.stp24.model.game.Fleet;

public record ShipGroup (
        Fleet fleet,
        String type,
        Integer currentAmount
) {
}

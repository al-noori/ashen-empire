package de.uniks.stp24.dto;

public enum Tag {
    SOCIETY("Society"),
    PHYSICS("Physics"),
    ENGINEERING("Engineering"),
    MILITARY("Military"),
    ECONOMY("Economy"),
    STATE("State"),
    BIOLOGY("Biology"),
    ENERGY("Energy"),
    COMPUTING("Computing"),
    PROPULSION("Propulsion"),
    MATERIALS("Materials"),
    CONSTRUCTION("Construction"),
    PRODUCTION("Production"),
    RARE("Rare");

    private final String displayName;

    Tag(String displayName) {
        this.displayName = displayName;
    }
    public String getDisplayName() {
        return displayName;
    }
}

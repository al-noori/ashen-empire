package de.uniks.stp24.dto;

import java.util.Map;

public record UpdateFractionDto(        String name,
        Map<String,Integer> districts,
        String[] buildings,
        String  upgrade,//Use enum here for, for details look at swagger
        String owner,
        Object _public
        ) {
}

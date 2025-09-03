package de.uniks.stp24.service;

import de.uniks.stp24.rest.GameSystemsApiService;

import javax.inject.Inject;
import java.util.Map;

public class DistrictService {

    @Inject
    GameSystemsApiService gameSystemsApiService;

    @Inject
    public DistrictService() {
    }

    public final Map<String, String> districtIDtoName = Map.ofEntries(
            Map.entry("city", "Market square"),
            Map.entry("energy", "Energy Production Area"),
            Map.entry("mining", "Military Sector"),
            Map.entry("agriculture", "Agricultural Sector"),
            Map.entry("industry", "Manufacturing Hub"),
            Map.entry("research_site", "Scientific sector"),
            Map.entry("ancient_foundry", "Metal foundry"),
            Map.entry("ancient_factory", "Medical zone"),
            Map.entry("ancient_refinery", "Oil refinery")
    );


    public String getDistrictName(String districtID) {
        return districtIDtoName.get(districtID);
    }
}

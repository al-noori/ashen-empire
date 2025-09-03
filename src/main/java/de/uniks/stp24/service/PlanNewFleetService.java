package de.uniks.stp24.service;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class PlanNewFleetService {

    private final Map<String, Integer> shipCounters = new HashMap<>();

    @Inject
    public PlanNewFleetService() {
    }

    public void resetShipCounters() {
        shipCounters.clear();
    }

    public void incrementShipCounter(String shipId) {
        shipCounters.put(shipId, shipCounters.getOrDefault(shipId, 0) + 1);
    }

    public void decrementShipCounter(String shipId) {
        shipCounters.put(shipId, shipCounters.getOrDefault(shipId, 0) - 1);
    }

    public int getShipCounter(String shipId) {
        return shipCounters.getOrDefault(shipId, 0);
    }

    public Map<String, Integer> getShipCounters() {
        return shipCounters;
    }

}

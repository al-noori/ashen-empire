package de.uniks.stp24.service;

import de.uniks.stp24.dto.TraitsDto;
import de.uniks.stp24.rest.PresetsApiService;
import io.reactivex.rxjava3.core.Observable;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import org.fulib.fx.annotation.controller.Resource;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import java.util.*;

@Singleton
public class TraitsService {

    public final Map <String, TraitsDto> traitsCache = new HashMap<>();

    @Inject
    PresetsApiService presetsApiService;

    @Inject
    @Resource
    ResourceBundle resources;

    // needed for listeners
    public SimpleObjectProperty<Integer> counterSelectedTraits = new SimpleObjectProperty<>(0);
    public SimpleObjectProperty<Integer> cost = new SimpleObjectProperty<>(5);
    public SimpleListProperty<String> selectedTraitIds = new SimpleListProperty<>(FXCollections.observableArrayList());

    @Inject
    public TraitsService() {
    }

    public Observable<Collection<TraitsDto>> loadTraits() {
        if (traitsCache.isEmpty()) {
            return presetsApiService.getTraits().map(traits -> {
                synchronized (traitsCache) {
                    List<TraitsDto> traitsDtos = traits.body();
                    traitsCache.clear();
                    if (traitsDtos != null) {
                        traitsDtos.removeIf(traitsDto -> Objects.equals(traitsDto.id(), "__dev__"));
                        traitsDtos.forEach(traitsDto -> traitsCache.put(traitsDto.id(), traitsDto));
                    }
                }
                return traits.body();
            });
        }
        return Observable.just(traitsCache.values());
    }

    public String getTraitName(String traitID) {
        return resources.getString("traitsName." + traitID);
    }

    public String getEffectName(String effectVariable) {
        try {
            if (effectVariable.startsWith("resources.")) {
                String resource = effectVariable.split("\\.")[1];
                return resources.getString("effect.starting") + " " + resources.getString("effect." + resource);
            } else {
                String building = effectVariable.split("\\.")[1];
                String resource = effectVariable.split("\\.")[3];
                return resources.getString("effect." + building) + " " + resources.getString("effect." + resource) + " " + resources.getString("effect.production");
            }
        } catch (Exception e) {
            return effectVariable;
        }
    }

    public String getEffectMultiplier(double multiplier) {
        double percentage = (multiplier - 1.0) * 100.0;
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        String formattedPercentage = decimalFormat.format(percentage);
        if (percentage > 0) {
            return "+" + formattedPercentage + "%";
        }
        return formattedPercentage + "%";
    }

    public TraitsDto getTrait(String traitID) {
        return traitsCache.get(traitID);
    }

    public int getSelectedTraits() {
        return counterSelectedTraits.getValue();
    }

    public void addTrait(String id) {
        selectedTraitIds.add(id);
        cost.set(cost.getValue() - traitsCache.get(id).cost());
        counterSelectedTraits.set(counterSelectedTraits.getValue() + 1);
    }

    public void removeTrait(String id) {
        selectedTraitIds.remove(id);
        cost.set(cost.getValue() + traitsCache.get(id).cost());
        counterSelectedTraits.set(counterSelectedTraits.getValue() - 1);
    }

    public int getTraitPoints() {
        return cost.getValue();
    }

    public void clear() {
        selectedTraitIds.clear();
        cost.set(5);
        counterSelectedTraits.set(0);
    }

}

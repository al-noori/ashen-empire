package de.uniks.stp24.service;

import de.uniks.stp24.dto.EmpireDto;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class EmpireService {
    String game;
    List<EmpireDto> empireList = new ArrayList<>();

    @Inject
    public EmpireService() {
    }

    public void createEmpire(String game, EmpireDto empireDto) {
        validate(game);
        empireList.add(empireDto);
    }

    public List<EmpireDto> getEmpires(String game) {
        validate(game);
        return empireList;
    }

    private void validate(String game) {
        if (!game.equals(this.game)) {
            empireList.clear();
            this.game = game;
        }
    }
}

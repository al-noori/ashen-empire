package de.uniks.stp24.model.game;

import org.fulib.builder.ClassModelDecorator;
import org.fulib.builder.ClassModelManager;
import org.fulib.builder.reflect.Link;
import org.fulib.builder.reflect.Type;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class GenModel implements ClassModelDecorator {
    class Game {
        LocalDateTime createdAt;
        LocalDateTime updatedAt;
        String id;
        User owner;
        String name;
        boolean started;
        int speed;
        int period;
        int size;
        int playersCount;
        int maxPlayers;
        @Link("game")
        CopyOnWriteArrayList<Fraction> fractions;
        @Link("game")
        CopyOnWriteArrayList<User> members;
    }

    class Fraction {
        LocalDateTime createdAt;
        LocalDateTime updatedAt;
        String _id;
        @Link("fractions")
        Game game;
        String name;
        int capacity;
        String upgrade;
        int population;
        int x;
        int y;
        @Link("fractions")
        Empire empire;
        @Link("links")
        CopyOnWriteArrayList<Fraction> links;
        @Link("homeSystem")
        Empire homeEmpire;
        CopyOnWriteArrayList<String> buildings;
        Map<String, Integer> districts;
        Map<String, Integer> districtSlots;
        String type;
        @Link("fraction")
        CopyOnWriteArrayList<Jobs> jobs;
        @Link("location")
        CopyOnWriteArrayList<Fleet> fleets;
        @Link("path")
        CopyOnWriteArrayList<Jobs> currentTraveling;
        Integer health;
    }

    class User {
        @Link("members")
        Game game;
        String _id;
        String name;
        LocalDateTime createdAt;
        LocalDateTime updatedAt;
        boolean ready;
        @Link("owner")
        Empire empire;
    }

    class Empire {
        @Link("empire")
        User owner;
        String _id;
        String name;
        String description;
        String color;
        int flag;
        int portrait;
        CopyOnWriteArrayList<String> traits;
        @Link("empire")
        CopyOnWriteArrayList<Fraction> fractions;
        @Link("homeEmpire")
        Fraction homeSystem;
        LocalDateTime createdAt;
        LocalDateTime updatedAt;
        Map<String, Integer> resources;
        @Link("empire")
        CopyOnWriteArrayList<Jobs> jobs;
        @Link("empire")
        CopyOnWriteArrayList<Technology> technologies;
        @Link("empire")
        CopyOnWriteArrayList<Fleet> fleets;
        @Link("empire")
        CopyOnWriteArrayList<Ship> ships;
        @Link("attacker")
        CopyOnWriteArrayList<War> attackingWars;
        @Link("defender")
        CopyOnWriteArrayList<War> defendingWars;
    }

    class Jobs {
        LocalDateTime createdAt;
        LocalDateTime updatedAt;
        String _id;
        double progress;
        double total;
        @Link("jobs")
        Empire empire;
        @Link("jobs")
        Fraction fraction;
        int priority;
        String type;
        String building;
        String district;
        String technology;
        String ship;
        Map<String, Integer> cost;
        @Type("de.uniks.stp24.dto.ResultDto")
        Object result;
        @Link("moving")
        Fleet fleet;
        @Link("currentTraveling")
        CopyOnWriteArrayList<Fraction> path;
    }
    class Technology {
        LocalDateTime createdAt;
        LocalDateTime updatedAt;
        String _id;
        @Link("technologies")
        Empire empire;
        int cost;
        CopyOnWriteArrayList<String> requires;
        CopyOnWriteArrayList<String> precedes;
        CopyOnWriteArrayList<String> tags;
    }
    class Fleet {
        LocalDateTime createdAt;
        LocalDateTime updatedAt;
        String _id;
        @Link("fleets")
        Empire empire;
        String name;
        @Link("fleets")
        Fraction location;
        Map<String, Integer> size;
        @Link("fleet")
        CopyOnWriteArrayList<Ship> ships;
        @Link("fleet")
        Jobs moving;
    }
    class Ship {
        LocalDateTime createdAt;
        LocalDateTime updatedAt;
        String _id;
        @Link("ships")
        Empire empire;
        @Link("ships")
        Fleet fleet;
        String type;
        Integer health;
        Integer experience;
    }
    class War {
        LocalDateTime createdAt;
        LocalDateTime updatedAt;
        String _id;
        @Link("defendingWars")
        Empire defender;
        @Link("attackingWars")
        Empire attacker;
        String name;
    }


    @Override
    public void decorate(ClassModelManager mm) {
        mm.haveNestedClasses(GenModel.class);
    }
}
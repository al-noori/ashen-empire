package de.uniks.stp24.constants;

import de.uniks.stp24.model.Achievement;

import java.util.Map;

public class AchievementsConstants {
    public static final Map<String, Achievement> ACHIEVEMENTS = Map.of(
            "507f191e810c19729de860ec", new Achievement("First Steps", "Played your very first game", 0, 1, 0),
            "507f191e810c19729de860ed", new Achievement("Beginner's Luck", "Won 1 game", 0, 1, 0),
            "507f191e810c19729de860ee", new Achievement("Double Win", "Won 2 games", 0, 2, 0),
            "507f191e810c19729de860ef", new Achievement("Triple Threat", "Won 3 games", 0, 3, 0),
            "507f191e810c19729de860f0", new Achievement("Quadruple Victory", "Won 4 games", 0, 4, 0),
            "507f191e810c19729de860f1", new Achievement("Champion's Streak", "Won 5 games", 0, 5, 0),
            "507f191e810c19729de860f2", new Achievement("Master of Ceremonies", "Won a game hosted by you", 0, 1, 0),
            "507f191e810c19729de860f3", new Achievement("Tech Titan", "Unlocked all technologies in a game", 0, 1, 0),
            "507f191e810c19729de860f4", new Achievement("Energy Hoarder", "Saved 10,000 energy", 0, 1, 0),
            "507f191e810c19729de860f5", new Achievement("Treasure Trove", "Saved 20,000 coins", 0, 1, 0)
    );
}

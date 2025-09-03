package de.uniks.stp24.model;

import java.time.LocalDateTime;

public class Achievement {
    private final String name;
    private final String description;
    private final int minProgress;
    private final int maxProgress;
    private  int progress;
    private LocalDateTime unlockedAt = null;

    public Achievement(String name, String description, int minProgress, int maxProgress, int progress) {
        this.name = name;
        this.description = description;
        this.minProgress = minProgress;
        this.maxProgress = maxProgress;
        this.progress = progress;
    }
    public Achievement(String name, String description, int minProgress, int maxProgress, int progress, LocalDateTime unlockedAt) {
        this.name = name;
        this.description = description;
        this.minProgress = minProgress;
        this.maxProgress = maxProgress;
        this.progress = progress;
        this.unlockedAt = unlockedAt;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getMinProgress() {
        return minProgress;
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    public int getProgress() {
        return progress;
    }

    public LocalDateTime getUnlockedAt() {
        return unlockedAt;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}

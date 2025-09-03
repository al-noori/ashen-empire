package de.uniks.stp24.service;

import javax.inject.Inject;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.inject.Singleton;
import java.util.Objects;

@Singleton
public class MusicService {

    private MediaPlayer mediaPlayer;
    private boolean isMuted = false;

    @Inject
    public MusicService() {
        if (!isMusicDisabled()) {
            playMusic();
        }
    }

    public void playMusic() {
        if (mediaPlayer == null || mediaPlayer.getStatus() == MediaPlayer.Status.STOPPED) {
            String musicFile = Objects.requireNonNull(getClass().getResource("/de/uniks/stp24/music/song.mp3")).toExternalForm();
            Media sound = new Media(musicFile);
            mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.setOnReady(() -> {
                if (!isMuted) {
                    mediaPlayer.play();
                }
            });
        } else if (!isMuted && (mediaPlayer.getStatus() == MediaPlayer.Status.READY || mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED)) {
            mediaPlayer.play();
        }
    }

    public void toggleMusic() {
        if (isMuted()) {
            unmuteMusic();
            playMusic();
        } else {
            muteMusic();
        }
    }

    public void muteMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(0.0);
            isMuted = true;
        }
    }

    public void unmuteMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(1.0);
            isMuted = false;
        }
    }

    public boolean isMuted() {
        return isMuted;
    }

    private boolean isMusicDisabled() {
        // Check for a system property or environment variable
        return Boolean.getBoolean("music.disabled") || "true".equalsIgnoreCase(System.getenv("MUSIC_DISABLED"));
    }


}
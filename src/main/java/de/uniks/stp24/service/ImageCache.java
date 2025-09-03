package de.uniks.stp24.service;

import de.uniks.stp24.Main;
import javafx.scene.image.Image;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class ImageCache {
    private final Map<String, Image> images = new ConcurrentHashMap<>();

    @Inject
    public ImageCache() {
    }

    public Image get(String path) throws IllegalArgumentException {
        return get(path, true);
    }

    public Image get(String path, boolean background) throws IllegalArgumentException {
        synchronized (images) {
            return images.computeIfAbsent(path, k -> load(path, background));
        }
    }

    private Image load(String path, boolean background) throws IllegalArgumentException {
        if (!path.startsWith("http://") && !path.startsWith("https://") && !path.startsWith("file://") && !path.startsWith("data:")) {
            final URL url = Main.class.getResource(path);
            if (url != null) {
                path = url.toExternalForm();
            } else {
                System.err.println("Failed to load image: " + path);
                return new Image("https://via.placeholder.com/150?text=Image+not+found", true);
            }
        }
        return new Image(path, background);
    }

    public void clear() {
        images.clear();
    }
}
package de.uniks.stp24;

import javafx.application.Application;

import java.util.Map;

public class Main {
    public static final String SERVER_HOSTNAME;
    public static final int SERVER_PORT;
    public static final String SERVER_VERSION;
    public static final String CLIENT_SWITCH_FILE;

    static {
        final Map<String, String> getenv = System.getenv();
        SERVER_HOSTNAME = getenv.getOrDefault("SERVER_HOSTNAME", "stpellar.uniks.de");
        SERVER_PORT = Integer.parseInt(getenv.getOrDefault("SERVER_PORT", "443"));
        SERVER_VERSION = getenv.getOrDefault("SERVER_VERSION", "v4");
        CLIENT_SWITCH_FILE = getenv.getOrDefault("CLIENT_SWITCH_FILE", "clients.json");
    }

    public static final String WS_URL = (SERVER_PORT == 443 ? "wss" : "ws") + "://" + SERVER_HOSTNAME + ":" + SERVER_PORT + "/ws/" + SERVER_VERSION;
    public static final String API_URL = (SERVER_PORT == 443 ? "https" : "http") + "://" + SERVER_HOSTNAME + ":" + SERVER_PORT + "/api/" + SERVER_VERSION;

    public static void main(String[] args) {
        Application.launch(App.class, args);
    }
}


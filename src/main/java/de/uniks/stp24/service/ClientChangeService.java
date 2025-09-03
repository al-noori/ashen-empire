package de.uniks.stp24.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.uniks.stp24.App;
import de.uniks.stp24.Main;
import de.uniks.stp24.dto.Client;
import javafx.application.Platform;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static de.uniks.stp24.Main.CLIENT_SWITCH_FILE;

@Singleton
public class ClientChangeService {
    List<Client> clients;
    @Inject
    ObjectMapper objectMapper;
    @Inject
    App app;
    @Inject
    TokenStorage tokenStorage;
    Client ownClient;
    @Inject
    ClientChangeService() {
    }
    public void loadClients() {
        try {
            System.out.println("\033[0;34mCurrent directory: " + System.getProperty("user.dir") + "\033[0m");
            clients = new ArrayList<>();
            clients.addAll(List.of(objectMapper.readValue(new File(CLIENT_SWITCH_FILE), Client[].class)));
            clients.sort(Comparator.comparingInt(Client::stopPeriod).reversed());
            File thisClient = new File(Main.class.getProtectionDomain().getCodeSource().getLocation()
                    .toURI());
            for (Client client: clients) {
                File clientFile = new File(client.filename());
                if (thisClient.getCanonicalPath().equals(clientFile.getCanonicalPath())) {
                    ownClient = client;
                }
            }
            if (ownClient == null) {
                System.out.println("\033[0;33mAttention: Own client not found in clients.json! Client Change not operational!\033[0m");
            }
            System.out.println("\033[0;34mClients: " + clients + "\033[0m");
        }
        catch (Exception e) {
            System.out.println("No clients found");
        }
    }
    public void triggerClientChange(int period, String gameId) {
        if (clients == null || clients.isEmpty() || ownClient == null) return;
        if (ownClient.stopPeriod() > period) return;
        if (clients.indexOf(ownClient) == 0) {
            clients = null;
            return;
        }
        try {
            Client client = clients.get(clients.indexOf(ownClient) - 1);
            File clientFile = new File(client.filename());
            System.out.println("\033[0;34mStarting client: " + client.name() + "\033[0m");
            Runtime.getRuntime().exec(
                    new String[]{
                            "java",
                            "-Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8",
                            "-jar",
                            clientFile.getAbsolutePath(),
                            tokenStorage.getRefreshToken(),
                            gameId
                    }
            );
            app.stopWithoutLeavingGame();
            Platform.exit();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("\033[0;31mError executing client\033[0m");
        }
    }
}

package de.uniks.stp24.ws;

import jakarta.websocket.*;

import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.function.Consumer;

@jakarta.websocket.ClientEndpoint
public class ClientEndpoint {
    private final URI endpointURI;
    private final List<Consumer<String>> messageHandlers = Collections.synchronizedList(new ArrayList<>());
    Session userSession;

    public ClientEndpoint(URI endpointURI) {
        this.endpointURI = endpointURI;
    }

    public boolean isOpen() {
        return this.userSession != null && this.userSession.isOpen();
    }

    public void open() {
        if (isOpen()) {
            return;
        }
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, endpointURI);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @OnOpen
    public void onOpen(Session userSession) {
        this.userSession = userSession;
    }

    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        System.out.println(reason.getReasonPhrase());
        if (reason.getCloseCode().equals(CloseReason.CloseCodes.CLOSED_ABNORMALLY)) {
            open();
        } else {
            this.userSession = null;
        }
    }

    @OnMessage
    public void onMessage(String message) {
        synchronized (this.messageHandlers) {
            for (final Consumer<String> handler : this.messageHandlers) {
                handler.accept(message);
            }
        }
    }

    @OnError
    public void onError(Throwable error) {
        System.out.println("Error: " + error.getMessage());
    }

    public void addMessageHandler(Consumer<String> msgHandler) {
        synchronized (this.messageHandlers) {
            this.messageHandlers.add(msgHandler);
        }
    }

    public void removeMessageHandler(Consumer<String> msgHandler) {
        synchronized (this.messageHandlers) {
            this.messageHandlers.remove(msgHandler);
        }
    }

    public void sendMessage(String message) {
        if (this.userSession == null) {
            return;
        }
        this.userSession.getAsyncRemote().sendText(message);
    }

    public void close() {
        if (this.userSession == null) {
            return;
        }

        try {
            this.userSession.close();
        } catch (IOException e) {
            System.out.println("Error closing websocket: " + e.getMessage());
        }
    }

    public boolean hasMessageHandlers() {
        return !this.messageHandlers.isEmpty();
    }
}

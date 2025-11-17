package com.example;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public class HelloModel {

    private final NtfyConnection connection;
    private final ObservableList<NtfyMessageDto> messages = FXCollections.observableArrayList();
    /**
     * Create a HelloModel that uses the provided connection for message operations.
     *
     * @param connection the NtfyConnection used to fetch history, subscribe to live messages, and send messages or files
     */

    public HelloModel(NtfyConnection connection) {
        this.connection = connection;
    }

    /**
     * Provides the observable list of received and initially loaded messages.
     *
     * @return the ObservableList of NtfyMessageDto representing the model's current messages; changes to this list are observable by UI listeners
     */
    public ObservableList<NtfyMessageDto> getMessages() {
        return messages;
    }

    /**
     * Loads message history from the connection and updates the model's observable messages list.
     *
     * Fetches history on a background thread; on success replaces the contents of {@code messages}
     * on the JavaFX Application Thread and begins live streaming of new messages. Failures during
     * loading are ignored and do not modify the messages list.
     */

    public void loadInitialMessagesAsync() {
        CompletableFuture
                .supplyAsync(connection::fetchHistory)
                .thenAccept(list -> Platform.runLater(() -> {
                    messages.setAll(list);
                    subscribeLive(); // start streaming after history
                }))
                .exceptionally(ex -> null);
    }

    /**
     * Subscribes to the connection's live message stream and appends each received message to the model's observable messages list on the JavaFX application thread.
     */
    private void subscribeLive() {
        connection.receive(m -> Platform.runLater(() -> messages.add(m)));
    }

    /**
     * Sends a text message via the model's connection.
     *
     * @param text the message content to send
     * @return true if the message was sent successfully, false otherwise
     */

    public boolean sendMessage(String text) {
        return connection.send(text);
    }

    /**
     * Send the file at the given path using the configured connection.
     *
     * @param path the path to the file to send
     * @return `true` if the file was sent successfully, `false` otherwise
     * @throws FileNotFoundException if the file does not exist or cannot be opened
     */
    public boolean sendFile(Path path) throws FileNotFoundException {
        return connection.sendFile(path);
    }

    /**
     * Subscribes for incoming messages and appends each received message to the model's messages list.
     *
     * Each message is added on the JavaFX Application Thread so UI-bound observers of the
     * ObservableList will be updated safely.
     */
    public void receiveMessage() {
        connection.receive(m -> Platform.runLater(() -> messages.add(m)));
    }


}
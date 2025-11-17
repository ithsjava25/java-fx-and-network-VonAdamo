package com.example;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class NtfyConnectionSpy implements NtfyConnection {

    String message;

    /**
     * Records the provided message as the last message sent through this connection.
     *
     * @param message the message to store as the last sent message
     * @return `true` indicating the message was recorded
     */
    @Override
    public boolean send(String message) {
        this.message = message;
        return true;
    }

    /**
     * Registers a handler for incoming messages; in this spy implementation the handler is ignored.
     *
     * <p>This implementation performs no action and will not invoke the provided handler.
     *
     * @param messageHandler a Consumer to handle received NtfyMessageDto messages (ignored)
     */
    @Override
    public void receive(Consumer<NtfyMessageDto> messageHandler) {
    }

    /**
     * Retrieve historical messages from the connection.
     *
     * @return an empty, unmodifiable list of historical NtfyMessageDto objects
     */
    @Override
    public List<NtfyMessageDto> fetchHistory() {
        return Collections.emptyList();
    }
}
package com.example;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;

class NtfyConnectionSpy implements NtfyConnection {

    String lastMessage;
    Path lastFilePath;

    @Override
    public boolean send(String message) {
        this.lastMessage = message;
        return true;
    }

    @Override
    public void receive(Consumer<NtfyMessageDto> handler) {
    }

    @Override
    public List<NtfyMessageDto> fetchHistory() {
        return List.of();
    }

    @Override
    public boolean sendFile(Path path) throws FileNotFoundException {
        this.lastFilePath = path;
        return true;
    }
}

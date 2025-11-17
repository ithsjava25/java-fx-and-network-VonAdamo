package com.example;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

class NtfyConnectionStub implements NtfyConnection {

    boolean sendResult = true;
    List<NtfyMessageDto> history = new ArrayList<>();
    Consumer<NtfyMessageDto> liveHandler;

    @Override
    public boolean send(String message) {
        return sendResult;
    }

    @Override
    public void receive(Consumer<NtfyMessageDto> handler) {
        this.liveHandler = handler;
    }

    @Override
    public List<NtfyMessageDto> fetchHistory() {
        return history;
    }

    @Override
    public boolean sendFile(Path path) throws FileNotFoundException {
        return sendResult;
    }

    void simulateIncomingMessage(NtfyMessageDto dto) {
        if (liveHandler != null) {
            liveHandler.accept(dto);
        }
    }
}

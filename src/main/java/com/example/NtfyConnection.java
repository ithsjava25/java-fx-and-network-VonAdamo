package com.example;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;

public interface NtfyConnection {

    /**
 * Sends the given message over this connection.
 *
 * @param message the message text to send
 * @return true if the message was sent successfully, false otherwise
 */
boolean send(String message);

    /**
 * Registers a handler invoked for each incoming Ntfy message.
 *
 * @param messageHandler callback that will be invoked with every received {@code NtfyMessageDto}
 */
void receive(Consumer<NtfyMessageDto> messageHandler);

    /**
 * Retrieve the connection's previously sent or received messages.
 *
 * @return a list of past NtfyMessageDto messages; an empty list if no history is available.
 */
List<NtfyMessageDto> fetchHistory();

    /**
 * Sends the file located at the given filesystem path.
 *
 * @param path the filesystem path of the file to send
 * @return true if the file was sent successfully, false otherwise
 * @throws FileNotFoundException if the specified file does not exist or is not accessible
 */
boolean sendFile(Path path) throws FileNotFoundException;
}
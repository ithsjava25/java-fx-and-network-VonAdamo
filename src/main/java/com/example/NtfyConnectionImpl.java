package com.example;

import io.github.cdimascio.dotenv.Dotenv;
import tools.jackson.databind.ObjectMapper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class NtfyConnectionImpl implements NtfyConnection {

    private final HttpClient http = HttpClient.newHttpClient();
    private final String hostName;
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Constructs a connection using the HOST_NAME environment variable.
     *
     * Reads the HOST_NAME environment variable and uses its value as the connection host.
     *
     * @throws NullPointerException if the HOST_NAME environment variable is not set
     */
    public NtfyConnectionImpl() {
        Dotenv dotenv = Dotenv.load();
        hostName = Objects.requireNonNull(dotenv.get("HOST_NAME"));
    }

    /**
     * Create an NtfyConnectionImpl that will communicate with the specified ntfy service host.
     *
     * @param hostName the base host URL for the ntfy service (e.g., "https://ntfy.example.com"); must not be null
     */
    public NtfyConnectionImpl(String hostName) {
        this.hostName = hostName;
    }

    /**
     * Sends the provided text as an HTTP POST to the configured topic endpoint.
     *
     * @param message the text payload to send in the request body
     * @return `true` if the HTTP request completed successfully, `false` otherwise
     */
    @Override
    public boolean send(String message) {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(message))
                .header("Content-Type", "text/plain; charset=utf-8")
                .uri(URI.create(hostName + "/mytopic"))
                .build();

        try {
            http.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            return true;
        } catch (IOException e) {
            System.out.println("Error sending message");
        } catch (InterruptedException e) {
            System.out.println("Interrupted sending message");
            Thread.currentThread().interrupt();
        }
        return false;
    }

    /**
     * Uploads the given file to the configured host's "/mytopic" endpoint using an HTTP PUT request.
     *
     * The request will include a "Filename" header set to the file's name and the file's bytes as the request body.
     *
     * @param file the path to the file to upload; its file name is used for the "Filename" header
     * @return {@code true} if the HTTP request completed successfully, {@code false} otherwise
     * @throws FileNotFoundException if the file cannot be opened for reading
     */
    public boolean sendFile(Path file) throws FileNotFoundException {
        String filename = file.getFileName().toString();

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .PUT(HttpRequest.BodyPublishers.ofFile(file))
                .header("Filename", filename)
                .uri(URI.create(hostName + "/mytopic"))
                .build();

        try {
            http.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            return true;
        } catch (IOException e) {
            System.out.println("Error sending file");
        } catch (InterruptedException e) {
            System.out.println("Interrupted sending file");
            Thread.currentThread().interrupt();
        }
        return false;
    }

    /**
     * Starts receiving events from the server and forwards each parsed "message" event to the provided handler.
     *
     * Initiates a non-blocking GET request to the connection's topic endpoint, parses each incoming line into a
     * NtfyMessageDto (ignoring lines that fail to parse), filters for events where `event()` equals "message",
     * and invokes the provided handler for each such message.
     *
     * @param messageHandler consumer invoked for every received `NtfyMessageDto` whose `event()` equals "message"
     */
    @Override
    public void receive(Consumer<NtfyMessageDto> messageHandler) {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(hostName + "/mytopic/json"))
                .build();

        http.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofLines())
                .thenAccept(response ->
                        response.body()
                        .map(this::tryParse)
                        .filter(m -> "message".equals(m.event()))
                        .forEach(messageHandler));
    }

    /**
     * Fetches and parses the message history from the configured topic.
     *
     * @return a list of NtfyMessageDto instances whose `event()` equals "message"; returns an empty list if fetching or parsing fails.
     *         If the operation is interrupted, the thread's interrupt status is restored and an empty list is returned.
     */
    @Override
    public List<NtfyMessageDto> fetchHistory() {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(hostName + "/mytopic/json?poll=1&since=all"))
                .build();
        List<NtfyMessageDto> list = new ArrayList<>();
        try {
            HttpResponse<java.util.stream.Stream<String>> response =
                    http.send(request, HttpResponse.BodyHandlers.ofLines());
            response.body()
                    .map(this::tryParse)
                    .filter(Objects::nonNull)
                    .filter(m -> "message".equals(m.event()))
                    .forEach(list::add);
        } catch (IOException | InterruptedException e) {
            if (e instanceof InterruptedException) Thread.currentThread().interrupt();
        }
        return list;
    }

    /**
     * Parses a single JSON line into an NtfyMessageDto.
     *
     * @param line the JSON text to parse
     * @return the parsed NtfyMessageDto, or {@code null} if the input cannot be parsed
     */
    private NtfyMessageDto tryParse(String line) {
        try {
            return mapper.readValue(line, NtfyMessageDto.class);
        } catch (Exception ignored) {
            return null;
        }
    }
}
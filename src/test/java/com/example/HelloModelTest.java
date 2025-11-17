package com.example;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@WireMockTest
class HelloModelTest {

    @BeforeAll
    static void initFx() {
        // Skip JavaFX initialization on headless/CI to avoid Glass failures
        if (GraphicsEnvironment.isHeadless() || "true".equalsIgnoreCase(System.getenv("CI"))) {
            return;
        }
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException | IllegalArgumentException ignored) {
            // Already initialized or not applicable
        }
    }

    @Test
    void sendMessageCallsConnectionWithExplicitText() {
        var spy = new NtfyConnectionSpy();
        var model = new HelloModel(spy);

        model.sendMessage("Hello World");

        assertThat(spy.lastMessage).isEqualTo("Hello World");
    }

    @Test
    void sendMessageToFakeServer(WireMockRuntimeInfo wmRuntimeInfo) {
        var connection = new NtfyConnectionImpl("http://localhost:" + wmRuntimeInfo.getHttpPort());
        var model = new HelloModel(connection);
        stubFor(post("/mytopic").willReturn(ok()));

        model.sendMessage("Hello World");

        verify(postRequestedFor(urlEqualTo("/mytopic"))
                .withRequestBody(containing("Hello World")));

    }

    @Test
    void sendMessageDelegatesToConnectionAndReturnsTrue() {
        var stub = new NtfyConnectionStub();
        stub.sendResult = true;
        var model = new HelloModel(stub);

        boolean result = model.sendMessage("Test Message");

        assertThat(result).isTrue();
    }

}
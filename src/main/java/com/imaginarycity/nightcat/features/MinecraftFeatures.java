package com.imaginarycity.nightcat.features;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.imaginarycity.nightcat.util.JSONUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public final class MinecraftFeatures {

    private static final String USERNAME_TO_UUID_URL =
            "https://api.mojang.com/users/profiles/minecraft/%s";

    private static final HttpClient httpClient = HttpClient.newBuilder().build();

    private MinecraftFeatures() {}

    public static CompletableFuture<@Nullable String> getUUIDByName(@NotNull final String name) {
        final Function<String, @Nullable String> extractId = json -> {
            try {
                return JSONUtils.parse(json).get("id").asText();
            } catch (final JsonProcessingException e) {
                return null;
            }
        };

        final var uri = URI.create(String.format(USERNAME_TO_UUID_URL, name));
        final var request = HttpRequest.newBuilder()
                .uri(uri)
                .timeout(Duration.ofSeconds(10))
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(extractId);
    }
}

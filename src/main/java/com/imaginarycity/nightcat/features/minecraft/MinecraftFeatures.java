package com.imaginarycity.nightcat.features.minecraft;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.imaginarycity.nightcat.util.JSONUtils;
import lombok.NonNull;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public final class MinecraftFeatures {

    private static final String USERNAME_TO_UUID_URL =
            "https://api.mojang.com/users/profiles/minecraft/%s";

    private static final HttpClient httpClient = HttpClient.newBuilder().build();

    private MinecraftFeatures() {}

    public static CompletableFuture<Optional<String>> getUUIDByName(@NonNull final String name) {
        final Function<String, Optional<String>> extractId = json -> {
            try {
                return Optional.ofNullable(JSONUtils.parse(json).get("id"))
                        .map(JsonNode::asText);
            } catch (final JsonProcessingException e) {
                return Optional.empty();
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

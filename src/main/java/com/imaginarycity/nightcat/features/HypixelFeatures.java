package com.imaginarycity.nightcat.features;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.imaginarycity.nightcat.Configuration;
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

public final class HypixelFeatures {

    private static final String BASE_URL = "https://api.hypixel.net";
    private static final String token = Configuration.hypixelApiToken;
    private static final HttpClient httpClient = HttpClient.newBuilder().build();

    private HypixelFeatures() {}

    public enum PlayerRank {
        DEFAULT("NONE"),
        VIP("VIP"),
        VIP_PLUS("VIP_PLUS"),
        MVP("MVP"),
        MVP_PLUS("MVP_PLUS"),
        MVP_PLUS_PLUS("SUPERSTAR"),
        YOUTUBER("YOUTUBER"),
        GAME_MASTER("GAME_MASTER"),
        ADMIN("ADMIN"),
        ;

        public final String codeName;

        PlayerRank(@NotNull final String name) {
            this.codeName = name;
        }
    }

    public static CompletableFuture<@Nullable JsonNode> getPlayerDataByUUID(@NotNull final String uuid) {
        final Function<String, @Nullable JsonNode> jsonParse = str -> {
            try {
                return JSONUtils.parse(str);
            } catch (final JsonProcessingException e) {
                return null;
            }
        };

        final var stringUri = String.format("%s/player?uuid=%s", BASE_URL, uuid);
        final var uri = URI.create(stringUri);
        final var request = HttpRequest.newBuilder(uri)
                .header("API-key", token)
                .timeout(Duration.ofSeconds(10))
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(jsonParse);
    }
}

package com.imaginarycity.nightcat.features.hypixel;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.imaginarycity.nightcat.Configuration;
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

public final class HypixelFeatures {

    private static final String BASE_URL = "https://api.hypixel.net";
    private static final String token = Configuration.hypixelApiToken;
    private static final HttpClient httpClient = HttpClient.newBuilder().build();

    private static final int MAX_EXP_NEEDED = 3_000_000;
    private static final int[] EXP_NEEDED = {
            100_000, // Lvl 0 -> Lvl 1
            150_000, // Lvl 1 -> Lvl 2
            250_000, // Lvl 2 -> Lvl 3
            500_000, // Etc
            750_000,
            1_000_000,
            1_250_000,
            1_500_000,
            2_000_000,
            2_500_000,
            2_500_000,
            2_500_000,
            2_500_000,
            2_500_000,
            MAX_EXP_NEEDED
    };

    private HypixelFeatures() {}

    public static CompletableFuture<Optional<JsonNode>> getPlayerDataByUUID(@NonNull final String uuid) {
        final Function<String, Optional<JsonNode>> extractPlayerData = str -> {
            try {
                return Optional.ofNullable(JSONUtils.parse(str).get("player"));
            } catch (final JsonProcessingException e) {
                return Optional.empty();
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
                .thenApply(extractPlayerData);
    }

    public static PlayerRank getHighestPlayerRank(@NonNull final JsonNode node) {
        if (hasRankField(node, "rank"))
            return getRankField(node, "rank");
        else if (hasRankField(node, "monthlyPackageRank"))
            return getRankField(node, "monthlyPackageRank");
        else if (hasRankField(node, "newPackageRank"))
            return getRankField(node, "newPackageRank");
        else if (hasRankField(node, "packageRank"))
            return getRankField(node, "packageRank");

        return PlayerRank.DEFAULT;
    }

    private static boolean hasRankField(final JsonNode node, final String fieldName) {
        final var nullableValue = node.get(fieldName);
        final var value = nullableValue == null ? "" : nullableValue.asText();
        return !value.isEmpty() && !value.equals("NONE") && !value.equals("NORMAL");
    }

    private static PlayerRank getRankField(final JsonNode node, final String fieldName) {
        return PlayerRank.fromCodeName(node.get(fieldName).asText());
    }

    public static int getLevel(double exp) {
        if (exp < 0) {
            throw new IllegalArgumentException("exp < 0");
        }

        for (int level = 0; ; level++) {
            double needed = expToNextLevel(level);
            exp -= needed;

            if (exp < 0)
                return level;
        }
    }

    private static int expToNextLevel(final int level) {
        return level < EXP_NEEDED.length ? EXP_NEEDED[level] : MAX_EXP_NEEDED;
    }
}
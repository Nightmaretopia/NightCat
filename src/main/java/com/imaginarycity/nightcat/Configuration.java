package com.imaginarycity.nightcat;

import com.fasterxml.jackson.databind.JsonNode;
import com.imaginarycity.nightcat.util.JSONUtils;
import lombok.NonNull;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

public final class Configuration {

    public static final @NonNull JsonNode configNode;
    public static final @NonNull String discordBotToken;
    public static final @NonNull String hypixelApiToken;

    private Configuration() {}

    static {
        JsonNode tempConfigNode;
        String tempDiscordBotToken;
        String tempHypixelApiToken;

        try {
            final var configUrl = Configuration.class.getResource("/config.json");

            if (configUrl == null)
                throw new NoSuchFileException("Unable to locate config.json.");

            final var configPath = Path.of(configUrl.toURI());
            final var configContent = Files.readString(configPath);
            final var configJsonNode = JSONUtils.parse(configContent);

            tempConfigNode = configJsonNode;
            tempDiscordBotToken = configJsonNode.get("discord-bot-token").asText();
            tempHypixelApiToken = configJsonNode.get("hypixel-api-token").asText();
        } catch (final IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }

        configNode = tempConfigNode;
        discordBotToken = tempDiscordBotToken;
        hypixelApiToken = tempHypixelApiToken;
    }
}

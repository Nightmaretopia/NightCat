package com.imaginarycity.nightcat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.imaginarycity.nightcat.util.JSONUtils;
import lombok.NonNull;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Optional;

public final class Configuration {

    public static final @NonNull JsonNode configNode;
    public static final @NonNull String discordBotToken;
    public static final @NonNull String hypixelApiToken;

    private Configuration() {}

    static {
        try {
            final var configUrl = Configuration.class.getResource("/config.json");

            if (configUrl == null)
                throw new NoSuchFileException("Unable to locate config.json.");

            final var configPath = Path.of(configUrl.toURI());
            final var configContent = Files.readString(configPath);

            configNode = JSONUtils.parse(configContent);
            discordBotToken = getNode("discord-bot-token").asText();
            hypixelApiToken = getNode("hypixel-api-token").asText();
        } catch (final URISyntaxException | NoSuchFileException e) {
            throw new RuntimeException(e);
        } catch (final JsonProcessingException e) {
            throw new RuntimeException("Unable to parse config.json as JSON", e);
        } catch (final IOException e) {
            throw new RuntimeException("Unable to read config.json", e);
        }
    }

    public static JsonNode getNode(final String fieldName) {
        return Optional.ofNullable(configNode.get(fieldName))
                .orElseThrow(() -> new ConfigurationFileException(
                        "\"%s\" field not found".formatted(fieldName)));
    }

    public static class ConfigurationFileException extends IllegalArgumentException {
        public ConfigurationFileException(String s) {
            super(s);
        }
    }
}

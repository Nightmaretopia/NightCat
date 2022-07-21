package com.imaginarycity.nightcat.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;

import java.util.Objects;

public final class JSONUtils {
    public static final ObjectMapper mapper = new ObjectMapper();

    private JSONUtils() {}

    public static JsonNode parse(@NonNull final String content) throws JsonProcessingException {
        return mapper.readTree(Objects.requireNonNull(content));
    }
}

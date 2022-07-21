package com.imaginarycity.nightcat.util;

import com.imaginarycity.nightcat.Configuration;
import com.imaginarycity.nightcat.Main;
import com.imaginarycity.nightcat.features.hypixel.PlayerRank;

import java.util.ArrayList;
import java.util.List;

public final class RoleMappings {
    public static final List<RoleMapping<?>> all;

    static {
        final var errorMessageTemplate = "\"%s\" field at index %d of \"role-mappings\" is not found";

        final var roleMappingsNode = Configuration.getNode("role-mappings");
        final var mappingsSize = roleMappingsNode.size();
        final var roleMappings = new ArrayList<RoleMapping<?>>();

        for (int i = 0; i < mappingsSize; i++) {
            final var mappingNode = roleMappingsNode.get(i);
            final var requirement = mappingNode.get("requirement");
            final var roleIdNode = mappingNode.get("id");

            if (requirement == null)
                throw new Configuration.ConfigurationFileException(
                        errorMessageTemplate.formatted("requirement", i));
            if (roleIdNode == null)
                throw new Configuration.ConfigurationFileException(
                        errorMessageTemplate.formatted("id", i));

            final var roleId = roleIdNode.asText();
            if (requirement.isTextual()) {
                roleMappings.add(new RoleMapping.Rank(Main.jda, PlayerRank.fromCodeName(requirement.asText()), roleId));
            } else if (requirement.isInt()) {
                roleMappings.add(new RoleMapping.Level(Main.jda, requirement.asInt(), roleId));
            } else {
                throw new Configuration.ConfigurationFileException(
                        "Unexpected type of \"requirement\" field at %d of \"role-mappings\"".formatted(i)
                );
            }
        }

        all = roleMappings;
    }
}

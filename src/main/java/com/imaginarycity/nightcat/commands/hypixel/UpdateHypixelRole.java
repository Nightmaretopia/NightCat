package com.imaginarycity.nightcat.commands.hypixel;

import com.fasterxml.jackson.databind.JsonNode;
import com.imaginarycity.nightcat.Configuration;
import com.imaginarycity.nightcat.Main;
import com.imaginarycity.nightcat.commands.CustomCommand;
import com.imaginarycity.nightcat.features.hypixel.HypixelFeatures;
import com.imaginarycity.nightcat.features.hypixel.PlayerRank;
import com.imaginarycity.nightcat.features.minecraft.MinecraftFeatures;
import com.imaginarycity.nightcat.util.RoleMapping;
import com.imaginarycity.nightcat.util.RoleMappings;
import lombok.NonNull;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public final class UpdateHypixelRole extends CustomCommand {

    public UpdateHypixelRole() {
        super(
                Commands.slash("hypixel-stats", "Fetches the stats of a given player")
                        .addSubcommands(
                                new SubcommandData("id", "fetch via id")
                                        .addOption(
                                                OptionType.STRING,
                                                "player-id",
                                                "The UUID of the player",
                                                true),
                                new SubcommandData("name", "fetch via name")
                                        .addOption(
                                            OptionType.STRING,
                                            "player-name",
                                            "In-game name of the player",
                                            true)
                        )
        );
    }

    @Override
    public void execute(@NonNull final SlashCommandInteractionEvent event) {
        final var playerNameOptional = Optional.ofNullable(event.getOption("player-name"))
                .map(OptionMapping::getAsString);
        final var playerUuidOptional = Optional.ofNullable(event.getOption("player-id"))
                .map(OptionMapping::getAsString);

        if (playerNameOptional.isEmpty() && playerUuidOptional.isEmpty()) {
            event.reply("Something went horribly wrong...").queue();
            return;
        }

        final var optionalUuid = playerNameOptional
                .map(MinecraftFeatures::getUUIDByName)
                .flatMap(CompletableFuture::join);

        if (playerNameOptional.isPresent() && optionalUuid.isEmpty()) {
            event.reply("Cannot find a player with the name: " + playerNameOptional.get()).queue();
            return;
        }

        @SuppressWarnings("OptionalGetWithoutIsPresent")
        final var uuid = playerUuidOptional.orElse(optionalUuid.get());

        final var playerDataOptional = HypixelFeatures.getPlayerDataByUUID(uuid).join();
        final var playerRankOptional = playerDataOptional.map(HypixelFeatures::getHighestPlayerRank);
        final var playerLevelOptional = playerDataOptional
                .map(node -> node.get("networkExp"))
                .map(JsonNode::asDouble)
                .map(HypixelFeatures::getLevel);

        final var playerRank = playerRankOptional.orElse(PlayerRank.DEFAULT);
        final var playerLevel = playerLevelOptional.orElse(0);

        final var rolesToAdd = new ArrayList<Role>();

        RoleMappings.all.forEach(mapping -> {
            switch (mapping) {
                case RoleMapping.Rank rank -> {
                    if (playerRank.compareTo(rank.requirement) >= 0)
                        rolesToAdd.add(rank.role);
                }
                case RoleMapping.Level level -> {
                    if (playerLevel >= level.requirement)
                        rolesToAdd.add(level.role);
                }
            }
        });

        final var userSnowflake = UserSnowflake.fromId(event.getUser().getId());

        rolesToAdd.forEach(role ->
                Main.servingGuild.addRoleToMember(userSnowflake, role).queue());
    }
}

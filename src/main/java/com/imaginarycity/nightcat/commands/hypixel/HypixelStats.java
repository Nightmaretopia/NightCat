package com.imaginarycity.nightcat.commands.hypixel;

import com.imaginarycity.nightcat.commands.CustomCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.jetbrains.annotations.NotNull;

public final class HypixelStats extends CustomCommand {

    public HypixelStats() {
        super(
                Commands.slash("hypixel-stats", "Fetches the stats of a given player")
                        .addSubcommands(
                                new SubcommandData("id", "fetch via id")
                                        .addOption(
                                                OptionType.STRING,
                                                "player-id",
                                                "The UUID of the player",
                                                true
                                        ),
                                new SubcommandData("name", "fetch via name")
                                        .addOption(
                                            OptionType.STRING,
                                            "player-name",
                                            "In-game name of the player",
                                            true
                                        )
                        )
        );
    }

    @Override
    public void execute(@NotNull final SlashCommandInteractionEvent event) {

    }
}

package com.imaginarycity.nightcat.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class CustomCommand {
    public final CommandData commandData;

    protected CustomCommand(@NotNull final CommandData data) {
        commandData = data;
    }

    public abstract void execute(@NotNull SlashCommandInteractionEvent event);

    public static final List<CustomCommand> all = List.of();
}

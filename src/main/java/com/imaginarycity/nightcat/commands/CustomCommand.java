package com.imaginarycity.nightcat.commands;

import lombok.NonNull;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.List;

public abstract class CustomCommand {
    public final CommandData commandData;

    protected CustomCommand(@NonNull final CommandData data) {
        commandData = data;
    }

    public abstract void execute(@NonNull SlashCommandInteractionEvent event);

    public static final List<CustomCommand> all = List.of();
}

package com.imaginarycity.nightcat;

import com.imaginarycity.nightcat.commands.CustomCommand;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public final class EventHandler extends ListenerAdapter {
    private static final EventHandler eventHandler = new EventHandler();

    private EventHandler() {}

    public static EventHandler getEventHandler() {
        return eventHandler;
    }

    @Override
    public void onReady(@NotNull final ReadyEvent event) {
        final var jda = event.getJDA();
        final var allCommandData = CustomCommand.all.stream()
                .map(command -> command.commandData)
                .toList();

        jda.updateCommands().addCommands(allCommandData).queue();
    }
}

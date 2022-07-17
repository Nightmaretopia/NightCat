package com.imaginarycity.nightcat;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;
import java.util.EnumSet;

public final class Main {
    public static void main(final String[] args) throws LoginException {
        JDABuilder.create(EnumSet.noneOf(GatewayIntent.class))
                .setToken(Configuration.discordBotToken)
                .addEventListeners(EventHandler.getEventHandler())
                .build();
    }
}

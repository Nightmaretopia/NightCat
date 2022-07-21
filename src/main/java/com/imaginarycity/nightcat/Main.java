package com.imaginarycity.nightcat;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;
import java.util.EnumSet;

public final class Main {
    public static final JDA jda;
    public static final Guild servingGuild;

    static {
        try {
            jda = JDABuilder.create(EnumSet.noneOf(GatewayIntent.class))
                    .setToken(Configuration.discordBotToken)
                    .build();
            jda.awaitReady();

            servingGuild = jda.getGuildById(Configuration.getNode("serving-guild-id").asText());
        } catch (final LoginException e) {
            throw new RuntimeException("Unable to login!", e);
        } catch (final InterruptedException e) {
            throw new RuntimeException("jda.awaitReady() interrupted.", e);
        }
    }

    public static void main(final String[] args) {
        jda.addEventListener(EventHandler.getEventHandler());
    }
}

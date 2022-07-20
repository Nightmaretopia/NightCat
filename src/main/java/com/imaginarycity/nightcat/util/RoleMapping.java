package com.imaginarycity.nightcat.util;

import com.imaginarycity.nightcat.Configuration;
import lombok.NonNull;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Role;

import java.util.NoSuchElementException;

public sealed abstract class RoleMapping<T> permits RoleMapping.Rank, RoleMapping.Level {
    public final T requirement;
    public final Role role;
    protected RoleMapping(@NonNull final JDA jda,
                          @NonNull final T t,
                          @NonNull final String roleId) {
        final var guild = jda.getGuildById(Configuration.servingGuildId);
        if (guild == null)
            throw new NoSuchElementException("Can't find serving guild!");

        requirement = t;
        role = guild.getRoleById(roleId);
    }

    public static final class Rank extends RoleMapping<String> {
        public Rank(@NonNull JDA jda, @NonNull String s, @NonNull String roleId) {
            super(jda, s, roleId);
        }
    }

    public static final class Level extends RoleMapping<Integer> {
        public Level(@NonNull JDA jda, @NonNull Integer integer, @NonNull String roleId) {
            super(jda, integer, roleId);
        }
    }
}

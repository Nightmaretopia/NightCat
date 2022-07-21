package com.imaginarycity.nightcat.util;

import com.imaginarycity.nightcat.Main;
import com.imaginarycity.nightcat.features.hypixel.PlayerRank;
import lombok.NonNull;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Role;

public sealed abstract class RoleMapping<T> permits RoleMapping.Rank, RoleMapping.Level {
    public final T requirement;
    public final Role role;

    private RoleMapping(@NonNull final JDA jda,
                        @NonNull final T t,
                        @NonNull final String roleId) {
        requirement = t;
        role = Main.servingGuild.getRoleById(roleId);
    }

    public static final class Rank extends RoleMapping<PlayerRank> {
        Rank(@NonNull JDA jda,
             @NonNull PlayerRank rank,
             @NonNull String roleId) {
            super(jda, rank, roleId);
        }
    }

    public static final class Level extends RoleMapping<Integer> {
        Level(@NonNull JDA jda,
              @NonNull Integer requiredLevel,
              @NonNull String roleId) {
            super(jda, requiredLevel, roleId);
        }
    }
}

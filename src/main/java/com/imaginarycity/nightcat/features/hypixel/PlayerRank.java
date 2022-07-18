package com.imaginarycity.nightcat.features.hypixel;

import lombok.NonNull;

import java.util.List;

public enum PlayerRank implements Comparable<PlayerRank> {
    DEFAULT("NONE", "No Rank"),
    VIP("VIP", "VIP"),
    VIP_PLUS("VIP_PLUS", "VIP+"),
    MVP("MVP", "MVP"),
    MVP_PLUS("MVP_PLUS", "MVP+"),
    MVP_PLUS_PLUS("SUPERSTAR", "MVP++"),
    YOUTUBER("YOUTUBER", "YouTuber"),
    GAME_MASTER("GAME_MASTER", "Game Master"),
    ADMIN("ADMIN", "Admin"),
    ;

    public static final List<PlayerRank> ALL_RANKS = List.of(values());

    public final String codeName;
    public final String displayName;

    PlayerRank(@NonNull final String codeName, @NonNull final String displayName) {
        this.codeName = codeName;
        this.displayName = displayName;
    }

    public static PlayerRank fromCodeName(final @NonNull String codeName) {
        return ALL_RANKS.stream()
                .filter(rank -> rank.codeName.equalsIgnoreCase(codeName))
                .findAny()
                .orElse(DEFAULT);
    }
}

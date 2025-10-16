package me.brynner.ascendiaPrisonCore.data;

import java.util.UUID;

public class PlayerData {

    private final UUID uuid;
    private String rank;
    private int tokens;
    private int prestige;
    private boolean autosellEnabled = false;

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
        this.rank = "A";
        this.tokens = 0;
        this.prestige = 0;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public void addTokens(int amount) {
        this.tokens += amount;
    }


    public int getTokens() {
        return tokens;
    }

    public void setTokens(int tokens) {
        this.tokens = tokens;
    }

    public int getPrestige() {
        return prestige;
    }

    public void setPrestige(int prestige) {
        this.prestige = prestige;
    }

    public boolean isAutosellEnabled() {
        return autosellEnabled;
    }

    public void setAutosellEnabled(boolean autosellEnabled) {
        this.autosellEnabled = autosellEnabled;
    }


}

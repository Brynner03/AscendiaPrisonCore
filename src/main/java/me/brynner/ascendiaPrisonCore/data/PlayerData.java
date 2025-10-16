package me.brynner.ascendiaPrisonCore.data;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class PlayerData {

    private final Player player;

    private boolean autosellEnabled = false;
    private String rank = "A";
    private int prestige = 0;
    private double tokens = 0.0; // 🪙 NEW: Prison currency

    public PlayerData(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    // AUTOSMELL/SELL
    public boolean isAutosellEnabled() { return autosellEnabled; }
    public void setAutosellEnabled(boolean autosellEnabled) { this.autosellEnabled = autosellEnabled; }

    // RANK
    public String getRank() { return rank; }
    public void setRank(String rank) { this.rank = rank; }

    // PRESTIGE
    public int getPrestige() { return prestige; }
    public void setPrestige(int prestige) { this.prestige = prestige; }

    // TOKENS 🪙
    public double getTokens() { return tokens; }
    public void setTokens(double tokens) { this.tokens = tokens; }
    public void addTokens(double amount) { this.tokens += amount; }
    public void removeTokens(double amount) { this.tokens = Math.max(0, this.tokens - amount); }

    // -----------------------------
    // File save/load utilities
    // -----------------------------
    public void saveToFile(File file) {
        YamlConfiguration config = new YamlConfiguration();
        config.set("rank", rank);
        config.set("prestige", prestige);
        config.set("autosell", autosellEnabled);
        config.set("tokens", tokens); // 🪙 save tokens
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadFromFile(File file) {
        if (!file.exists()) return;
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        this.rank = config.getString("rank", "A");
        this.prestige = config.getInt("prestige", 0);
        this.autosellEnabled = config.getBoolean("autosell", false);
        this.tokens = config.getDouble("tokens", 0.0);
    }
}

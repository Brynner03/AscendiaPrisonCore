package me.brynner.ascendiaPrisonCore;

import me.brynner.ascendiaPrisonCore.commands.AutosellCommand;
import me.brynner.ascendiaPrisonCore.commands.RankupCommand;
import me.brynner.ascendiaPrisonCore.data.PlayerDataManager;
import me.brynner.ascendiaPrisonCore.listeners.AutosmeltListener;
import me.brynner.ascendiaPrisonCore.listeners.BlockBreakListener;
import me.brynner.ascendiaPrisonCore.listeners.PlayerJoinQuitListener;
import me.brynner.ascendiaPrisonCore.placeholders.PrisonExpansion;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class AscendiaPrisonCore extends JavaPlugin {

    private static AscendiaPrisonCore instance;
    private Economy economy;
    private PlayerDataManager playerDataManager;

    @Override
    public void onEnable() {
        instance = this;

        // Hook into Vault for Economy and Chat
        if (!setupEconomy()) {
            getLogger().severe("Vault or Chat provider not found! Disabling plugin...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Player data setup
        playerDataManager = new PlayerDataManager();

        // Hook into PlaceholderAPI
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PrisonExpansion(this).register();
            getLogger().info("PlaceholderAPI hooked successfully!");
        } else {
            getLogger().warning("PlaceholderAPI not found! Skipping placeholder registration.");
        }

        // Load config
        saveDefaultConfig();
        getLogger().info("Configuration loaded successfully!");

        // Initialize commands
        AutosellCommand autosellCommand = new AutosellCommand(this);
        getCommand("autosell").setExecutor(autosellCommand);
        getCommand("rankup").setExecutor(new RankupCommand(this));

        // Register listeners
        getServer().getPluginManager().registerEvents(new PlayerJoinQuitListener(playerDataManager, autosellCommand), this);
        getServer().getPluginManager().registerEvents(new AutosmeltListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(this), this);

        getLogger().info("Ascendia Prison Core enabled successfully!");
    }

    @Override
    public void onDisable() {
        if (playerDataManager != null) {
            playerDataManager.saveAll();
        }
        getLogger().info("Ascendia Prison Core disabled and player data saved.");
    }

    // -------------------------------
    // Vault Economy + Chat Integration
    // -------------------------------

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null)
            return false;
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null)
            return false;
        economy = rsp.getProvider();
        return economy != null;
    }

    // -------------------------------
    // Getters
    // -------------------------------

    public static AscendiaPrisonCore getInstance() {
        return instance;
    }

    public Economy getEconomy() {
        return economy;
    }


    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }
}

package me.brynner.ascendiaPrisonCore.listeners;

import me.brynner.ascendiaPrisonCore.AscendiaPrisonCore;
import me.brynner.ascendiaPrisonCore.commands.AutosellCommand;
import me.brynner.ascendiaPrisonCore.data.PlayerDataManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinQuitListener implements Listener {

    private final PlayerDataManager dataManager;
    private final AutosellCommand autosellCommand;

    public PlayerJoinQuitListener(PlayerDataManager dataManager, AutosellCommand autosellCommand) {
        this.dataManager = dataManager;
        this.autosellCommand = autosellCommand;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        dataManager.getData(player);
        AscendiaPrisonCore.getInstance().getLogger().info(player.getName() + " joined - PlayerData loaded.");
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        // Cancel autosell task if running
        autosellCommand.handlePlayerQuit(player.getUniqueId());

        // Disable autosell for the player
        dataManager.getData(player).setAutosellEnabled(false);

        // Unload their data
        dataManager.removeData(player);

        AscendiaPrisonCore.getInstance().getLogger().info(player.getName() + " left - Autosell canceled and PlayerData unloaded.");
    }
}

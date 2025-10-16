package me.brynner.ascendiaPrisonCore.listeners;

import me.brynner.ascendiaPrisonCore.AscendiaPrisonCore;
import me.brynner.ascendiaPrisonCore.data.PlayerDataManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinQuitListener implements Listener {

    private final PlayerDataManager dataManager;

    public PlayerJoinQuitListener(PlayerDataManager dataManager) {
        this.dataManager = dataManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        dataManager.getData(event.getPlayer());
        AscendiaPrisonCore.getInstance().getLogger().info(event.getPlayer().getName() + " joined - PlayerData loaded.");
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        dataManager.removeData(event.getPlayer());
        AscendiaPrisonCore.getInstance().getLogger().info(event.getPlayer().getName() + " left - PlayerData unloaded.");
    }
}

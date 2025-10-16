package me.brynner.ascendiaPrisonCore.listeners;

import me.brynner.ascendiaPrisonCore.AscendiaPrisonCore;
import me.brynner.ascendiaPrisonCore.data.PlayerData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import java.util.Random;

public class BlockBreakListener implements Listener {

    private final AscendiaPrisonCore plugin;
    private final Random random = new Random();

    public BlockBreakListener(AscendiaPrisonCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled()) return;

        PlayerData data = plugin.getPlayerDataManager().getData(event.getPlayer());

        int tokensGained = random.nextInt(5) + 1;

        data.addTokens(tokensGained);

        if (random.nextInt(100) < 10) { // 10% chance to show message
            event.getPlayer().sendMessage(Component.text("You have earned " + tokensGained + " Tokens! (Total: " + data.getTokens() + ")", NamedTextColor.AQUA));
        }
    }
}

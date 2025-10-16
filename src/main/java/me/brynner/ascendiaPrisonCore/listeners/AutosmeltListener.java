package me.brynner.ascendiaPrisonCore.listeners;

import me.brynner.ascendiaPrisonCore.AscendiaPrisonCore;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class AutosmeltListener implements Listener {

    private final AscendiaPrisonCore plugin;

    public AutosmeltListener(AscendiaPrisonCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled()) return;

        Player player = event.getPlayer();
        Block block = event.getBlock();
        Material type = block.getType();

        // Fetch smelted version from config
        if (plugin.getConfig().getConfigurationSection("smelt-map") == null) return;
        String smeltedName = plugin.getConfig().getString("smelt-map." + type.name());
        if (smeltedName == null) return;

        Material smelted = Material.matchMaterial(smeltedName);
        if (smelted == null) return;

        // Prevent vanilla drops
        event.setDropItems(false);
        block.setType(Material.AIR);

        // Drop smelted version instead
        block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(smelted, 1));

        // Simple feedback (sound + particle)
        player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_IRON, 0.4f, 1.5f);
        block.getWorld().spawnParticle(Particle.FLAME, block.getLocation().add(0.5, 0.5, 0.5), 5, 0.2, 0.2, 0.2, 0);
    }
}

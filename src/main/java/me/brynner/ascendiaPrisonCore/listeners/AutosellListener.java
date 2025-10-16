package me.brynner.ascendiaPrisonCore.listeners;

import me.brynner.ascendiaPrisonCore.AscendiaPrisonCore;
import me.brynner.ascendiaPrisonCore.data.PlayerData;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.text.DecimalFormat;

public class AutosellListener implements Listener {

    private final AscendiaPrisonCore plugin;
    private final DecimalFormat moneyFormat = new DecimalFormat("#,###");

    public AutosellListener(AscendiaPrisonCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled()) return;

        Player player = event.getPlayer();
        if (!player.hasPermission("ascendia.autosell")) return;

        PlayerData data = plugin.getPlayerDataManager().getData(player);
        if (!data.isAutosellEnabled()) return;

        Block block = event.getBlock();
        Material type = block.getType();

        double price = getDynamicBlockPrice(type, data.getRank(), data.getPrestige());
        if (price <= 0) return; // Not sellable

        Economy econ = plugin.getEconomy();
        econ.depositPlayer(player, price);

        // Remove the block
        block.setType(Material.AIR);

        // Subtle sound/particle feedback
        if (Math.random() < 0.08) {
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.4f, 1.2f);
            player.spawnParticle(Particle.HAPPY_VILLAGER, block.getLocation().add(0.5, 0.5, 0.5), 5);
        }

        // Occasional money message
        if (Math.random() < 0.10) {
            player.sendMessage(ChatColor.GREEN + "+$" + moneyFormat.format(price)
                    + ChatColor.GRAY + " (Balance: $"
                    + moneyFormat.format(econ.getBalance(player)) + ")");
        }
    }

    /**
     * Dynamic pricing system based on block type, rank, and prestige.
     */
    private double getDynamicBlockPrice(Material material, String rank, int prestige) {
        double basePrice;

        // Base block values
        switch (material) {
            case DIAMOND_BLOCK: basePrice = 5000; break;
            case EMERALD_BLOCK: basePrice = 7500; break;
            case GOLD_BLOCK: basePrice = 2500; break;
            case IRON_BLOCK: basePrice = 1500; break;
            case COAL_BLOCK: basePrice = 1000; break;
            case STONE: basePrice = 25; break;
            case COBBLESTONE: basePrice = 20; break;
            case NETHERRACK: basePrice = 10; break;
            default: return 0;
        }

        // Rank multiplier (A = 1, B = 2, etc.)
        String ranks = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int rankIndex = ranks.indexOf(rank.toUpperCase());
        if (rankIndex == -1) rankIndex = 0; // Default to rank A if invalid
        double rankMultiplier = 1.0 + (rankIndex * 0.5); // each rank adds +50%

        // Prestige multiplier (+5% per prestige)
        double prestigeMultiplier = 1.0 + (prestige * 0.05);

        // Combine all multipliers
        return basePrice * rankMultiplier * prestigeMultiplier;
    }
}

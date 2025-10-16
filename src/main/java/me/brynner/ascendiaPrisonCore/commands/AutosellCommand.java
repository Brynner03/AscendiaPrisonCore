package me.brynner.ascendiaPrisonCore.commands;

import me.brynner.ascendiaPrisonCore.AscendiaPrisonCore;
import me.brynner.ascendiaPrisonCore.data.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AutosellCommand implements CommandExecutor {

    private final AscendiaPrisonCore plugin;
    private final Map<UUID, Integer> autosellTasks = new HashMap<>();

    public AutosellCommand(AscendiaPrisonCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command!");
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("ascendia.autosell")) {
            player.sendMessage(ChatColor.RED + "You don’t have permission to use this command!");
            return true;
        }

        PlayerData data = plugin.getPlayerDataManager().getData(player);
        boolean newState = !data.isAutosellEnabled();
        data.setAutosellEnabled(newState);

        if (newState) {
            player.sendMessage(ChatColor.GREEN + "Autosell enabled! Selling all items now...");
            sellInventory(player);

            // Schedule repeating sell every 3 minutes (3 * 60 * 20 ticks)
            int taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
                if (data.isAutosellEnabled() && player.isOnline()) {
                    sellInventory(player);
                } else {
                    cancelAutosell(player);
                }
                // Delay and repeat every 3 min
            }, 20L * 180, 20L * 180);

            autosellTasks.put(player.getUniqueId(), taskId);

        } else {
            player.sendMessage(ChatColor.RED + "Autosell disabled!");
            cancelAutosell(player);
        }

        return true;
    }

    private void cancelAutosell(Player player) {
        Integer taskId = autosellTasks.remove(player.getUniqueId());
        if (taskId != null) {
            Bukkit.getScheduler().cancelTask(taskId);
        }
    }

    private void sellInventory(Player player) {
        double total = 0.0;

        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null) continue;

            double pricePer = getBlockPrice(item.getType());
            if (pricePer > 0) {
                total += pricePer * item.getAmount();
                player.getInventory().remove(item);
            }
        }

        if (total > 0) {
            plugin.getEconomy().depositPlayer(player, total);
            player.sendMessage(ChatColor.YELLOW + "Autosold inventory for " +
                    ChatColor.GREEN + "$" + String.format("%,.0f", total));
        }
    }

    private double getBlockPrice(Material material) {
        switch (material) {
            case DIAMOND_BLOCK: return 5000;
            case EMERALD_BLOCK: return 7500;
            case GOLD_BLOCK: return 2500;
            case IRON_BLOCK: return 1500;
            case COAL_BLOCK: return 1000;
            case STONE: return 25;
            case COBBLESTONE: return 20;
            case NETHERRACK: return 10;
            default: return 0;
        }
    }
}

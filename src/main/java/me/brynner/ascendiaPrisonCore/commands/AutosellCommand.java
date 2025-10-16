package me.brynner.ascendiaPrisonCore.commands;

import me.brynner.ascendiaPrisonCore.AscendiaPrisonCore;
import me.brynner.ascendiaPrisonCore.data.PlayerData;
import me.brynner.ascendiaPrisonCore.utils.PriceUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
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
            sender.sendMessage(Component.text("Only players can use this command!", NamedTextColor.RED));
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("ascendia.autosell")) {
            player.sendMessage(Component.text("You don’t have permission to use this command!", NamedTextColor.RED));
            return true;
        }

        PlayerData data = plugin.getPlayerDataManager().getData(player);
        boolean newState = !data.isAutosellEnabled();
        data.setAutosellEnabled(newState);

        if (newState) {
            player.sendMessage(Component.text("Autosell enabled!", NamedTextColor.GREEN));
            sellInventory(player, data);

            int interval = plugin.getConfig().getInt("autosell.interval-seconds", 180);
            long ticks = interval * 20L;

            int taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
                if (data.isAutosellEnabled() && player.isOnline()) {
                    sellInventory(player, data);
                } else {
                    cancelAutosell(player);
                }
            }, ticks, ticks);

            autosellTasks.put(player.getUniqueId(), taskId);

        } else {
            player.sendMessage(Component.text("Autosell disabled!", NamedTextColor.RED));
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

    private void sellInventory(Player player, PlayerData data) {
        double total = 0.0;

        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null) continue;

            Material type = item.getType();

            // Only sell items that exist in config
            if (!plugin.getConfig().contains("sellable-items." + type.name())) continue;

            // Get base value from config, then apply rank/prestige multipliers
            double basePrice = plugin.getConfig().getDouble("sellable-items." + type.name(), 0.0);
            double dynamicPrice = PriceUtil.getDynamicBlockPrice(type, basePrice, data.getRank(), data.getPrestige());


            // If you want to multiply config basePrice * dynamic multipliers instead of overriding, use:
            // double dynamicPrice = basePrice * PriceUtil.getDynamicMultiplier(data.getRank(), data.getPrestige());

            total += dynamicPrice * item.getAmount();
            player.getInventory().remove(item);
        }

        if (total > 0) {
            plugin.getEconomy().depositPlayer(player, total);

            Component message = Component.text("Autosold inventory for ", NamedTextColor.YELLOW)
                    .append(Component.text("$" + String.format("%,.0f", total), NamedTextColor.GREEN));
            player.sendMessage(message);
        }
    }
}

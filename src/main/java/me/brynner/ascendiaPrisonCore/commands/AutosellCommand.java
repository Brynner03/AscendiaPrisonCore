package me.brynner.ascendiaPrisonCore.commands;

import me.brynner.ascendiaPrisonCore.AscendiaPrisonCore;
import me.brynner.ascendiaPrisonCore.data.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AutosellCommand implements CommandExecutor {

    private final AscendiaPrisonCore plugin;

    public AutosellCommand(AscendiaPrisonCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command!");
            return true;
        }
        if (!sender.hasPermission("ascendia.autosell")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
            return true;
        }


        Player player = (Player) sender;
        PlayerData data = plugin.getPlayerDataManager().getData(player);

        boolean newState = !data.isAutosellEnabled();
        data.setAutosellEnabled(newState);

        String status = newState ? ChatColor.GREEN + "enabled" : ChatColor.RED + "disabled";
        player.sendMessage(ChatColor.YELLOW + "Autosell has been " + status + ChatColor.YELLOW + "!");
        return true;
    }
}

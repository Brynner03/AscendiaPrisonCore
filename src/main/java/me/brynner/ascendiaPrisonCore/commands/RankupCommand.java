package me.brynner.ascendiaPrisonCore.commands;

import me.brynner.ascendiaPrisonCore.AscendiaPrisonCore;
import me.brynner.ascendiaPrisonCore.data.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RankupCommand implements CommandExecutor {

    private final AscendiaPrisonCore plugin;

    public RankupCommand(AscendiaPrisonCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command!");
            return true;
        }

        Player player = (Player) sender;
        PlayerData data = plugin.getPlayerDataManager().getData(player);

        String currentRank = data.getRank();
        String nextRank = getNextRank(currentRank);

        if (nextRank == null) {
            player.sendMessage(ChatColor.GOLD + "You are at the highest rank!");
            return true;
        }

        double cost = getRankupCost(nextRank);
        double balance = plugin.getEconomy().getBalance(player);

        if (balance < cost) {
            player.sendMessage(ChatColor.RED + "You need $" + cost + " to rank up to " + nextRank + "!");
            return true;
        }

        // Deduct money and update rank
        plugin.getEconomy().withdrawPlayer(player, cost);
        data.setRank(nextRank);

        player.sendMessage(ChatColor.GREEN + "You ranked up to " + ChatColor.GOLD + nextRank + ChatColor.GREEN + "!");
        plugin.getLogger().info(player.getName() + " ranked up to " + nextRank);
        return true;
    }

    private String getNextRank(String currentRank) {
        String ranks = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int index = ranks.indexOf(currentRank);
        if (index == -1 || index + 1 >= ranks.length()) {
            return null;
        }
        return String.valueOf(ranks.charAt(index + 1));
    }

    private double getRankupCost(String rank) {
        String ranks = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int index = ranks.indexOf(rank);
        return 1000 * (index + 1);
    }
}

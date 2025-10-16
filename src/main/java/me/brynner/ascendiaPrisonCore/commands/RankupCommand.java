package me.brynner.ascendiaPrisonCore.commands;

import me.brynner.ascendiaPrisonCore.AscendiaPrisonCore;
import me.brynner.ascendiaPrisonCore.data.PlayerData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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
            sender.sendMessage(Component.text("Only players can use this command!", NamedTextColor.RED));
            return true;
        }

        Player player = (Player) sender;
        PlayerData data = plugin.getPlayerDataManager().getData(player);

        String currentRank = data.getRank();
        String nextRank = getNextRank(currentRank);

        if (nextRank == null) {
            player.sendMessage(Component.text("You are at the highest rank!", NamedTextColor.GOLD));
            return true;
        }

        double cost = getRankupCost(nextRank);
        double balance = plugin.getEconomy().getBalance(player);

        if (balance < cost) {
            Component message = Component.text("You need $", NamedTextColor.RED)
                    .append(Component.text(String.format("%,.0f", cost), NamedTextColor.GOLD))
                    .append(Component.text(" to rank up to ", NamedTextColor.RED))
                    .append(Component.text(nextRank, NamedTextColor.YELLOW))
                    .append(Component.text("!", NamedTextColor.RED));
            player.sendMessage(message);
            return true;
        }

        // Deduct money and update rank
        plugin.getEconomy().withdrawPlayer(player, cost);
        data.setRank(nextRank);

        Component success = Component.text("You ranked up to ", NamedTextColor.GREEN)
                .append(Component.text(nextRank, NamedTextColor.GOLD))
                .append(Component.text("!", NamedTextColor.GREEN));

        player.sendMessage(success);
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

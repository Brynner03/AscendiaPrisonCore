package me.brynner.ascendiaPrisonCore.placeholders;

import me.brynner.ascendiaPrisonCore.AscendiaPrisonCore;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class PrisonExpansion extends PlaceholderExpansion {

    private final AscendiaPrisonCore plugin;

    public PrisonExpansion(AscendiaPrisonCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getIdentifier() {
        return "ascendia";
    }

    @Override
    public String getAuthor() {
        return "Cirkitry";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        switch (identifier.toLowerCase()) {
            case "rank":
                return plugin.getPlayerDataManager()
                        .getData(player)
                        .getRank();
            case "tokens":
                return String.valueOf(plugin.getPlayerDataManager()
                        .getData(player)
                        .getTokens());
            case "prestige":
                return String.valueOf(plugin.getPlayerDataManager()
                        .getData(player)
                        .getPrestige());

            default:
                return null;
        }
    }
}

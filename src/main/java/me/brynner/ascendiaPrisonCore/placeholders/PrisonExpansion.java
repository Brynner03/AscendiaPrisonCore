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
        return "prison";
    }

    @Override
    public String getAuthor() {
        return "Brynner";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        switch (identifier.toLowerCase()) {
            case "rank":
                return "A"; // temporary test value
            case "tokens":
                return "0"; // temporary test value
            default:
                return null;
        }
    }
}

package me.brynner.ascendiaPrisonCore.utils;

import org.bukkit.Material;

public class PriceUtil {

    /**
     * Calculates the final sell price for a material, using the config base price
     * and applying dynamic multipliers for rank and prestige.
     */
    public static double getDynamicBlockPrice(Material material, double basePrice, String rank, int prestige) {
        if (basePrice <= 0) return 0;

        // Rank multiplier (A = 1, B = 2, etc.)
        String ranks = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int rankIndex = ranks.indexOf(rank.toUpperCase());
        if (rankIndex == -1) rankIndex = 0;
        double rankMultiplier = 1.0 + (rankIndex * 0.5);

        // Prestige multiplier (+5% per prestige)
        double prestigeMultiplier = 1.0 + (prestige * 0.05);

        // Return final dynamic value
        return basePrice * rankMultiplier * prestigeMultiplier;
    }
}

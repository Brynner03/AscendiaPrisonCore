package me.brynner.ascendiaPrisonCore.utils;

import org.bukkit.Material;

public class PriceUtil {

    /**
     * Dynamic pricing system based on block type, rank, and prestige.
     */
    public static double getDynamicBlockPrice(Material material, String rank, int prestige) {
        double basePrice;

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
        // Default to rank A if invalid
        if (rankIndex == -1) rankIndex = 0;
        double rankMultiplier = 1.0 + (rankIndex * 0.5);

        // Prestige multiplier (+5% per prestige)
        double prestigeMultiplier = 1.0 + (prestige * 0.05);

        return basePrice * rankMultiplier * prestigeMultiplier;
    }
}

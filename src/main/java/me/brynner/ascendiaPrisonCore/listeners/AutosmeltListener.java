package me.brynner.ascendiaPrisonCore.listeners;

import me.brynner.ascendiaPrisonCore.AscendiaPrisonCore;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Random;

public class AutosmeltListener implements Listener {

    private final AscendiaPrisonCore plugin;
    private final Random random = new Random();

    public AutosmeltListener(AscendiaPrisonCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled()) return;

        Player player = event.getPlayer();

        // 🚫 Skip creative players
        if (player.getGameMode() == GameMode.CREATIVE) return;

        Block block = event.getBlock();
        Material type = block.getType();

        ItemStack tool = player.getInventory().getItemInMainHand();

        // Respect Silk Touch
        if (tool.containsEnchantment(Enchantment.SILK_TOUCH)) return;

        // Check config mapping
        if (plugin.getConfig().getConfigurationSection("smelt-map") == null) return;
        String smeltedName = plugin.getConfig().getString("smelt-map." + type.name());
        if (smeltedName == null) return;

        Material smelted = Material.matchMaterial(smeltedName);
        if (smelted == null) return;

        // Cancel vanilla drops
        event.setDropItems(false);
        block.setType(Material.AIR);

        // Fortune check
        int fortuneLevel = tool.getEnchantmentLevel(Enchantment.FORTUNE);

        // Drop amount logic
        int amount = getDropAmount(type, fortuneLevel);
        ItemStack result = new ItemStack(smelted, amount);

        // Try to add directly to inventory
        HashMap<Integer, ItemStack> leftover = player.getInventory().addItem(result);

        // Drop leftovers on the ground if inventory is full
        leftover.values().forEach(item ->
                block.getWorld().dropItemNaturally(block.getLocation(), item));

        // Visual + audio feedback
        player.playSound(player.getLocation(), Sound.BLOCK_FURNACE_FIRE_CRACKLE, 0.5f, 1.3f);
        block.getWorld().spawnParticle(Particle.FLAME,
                block.getLocation().add(0.5, 0.5, 0.5),
                6, 0.25, 0.25, 0.25, 0.01);
    }

    private int getDropAmount(Material type, int fortune) {
        int base;
        switch (type) {
            case REDSTONE_ORE:
            case DEEPSLATE_REDSTONE_ORE:
                base = 4 + random.nextInt(2); // 4–5
                break;
            case LAPIS_ORE:
            case DEEPSLATE_LAPIS_ORE:
                base = 4 + random.nextInt(6); // 4–9
                break;
            case COPPER_ORE:
            case DEEPSLATE_COPPER_ORE:
                base = 2 + random.nextInt(4); // 2–5
                break;
            case NETHER_QUARTZ_ORE:
                base = 1 + random.nextInt(2); // 1–2
                break;
            case NETHER_GOLD_ORE:
                base = 2 + random.nextInt(5); // 2–6
                break;
            case ANCIENT_DEBRIS:
                base = 1; // always 1
                break;
            default:
                base = 1; // diamonds, emeralds, coal, etc.
                break;
        }

        // Apply Fortune multiplier
        if (fortune > 0) {
            int bonus = random.nextInt(fortune + 2) - 1;
            if (bonus < 0) bonus = 0;
            base *= (1 + bonus);
        }

        return base;
    }
}

package net.yinwu.lib.api;

import net.yinwu.lib.plugin.YinwuAPI;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * YinwuForge plugin API.
 *
 * <p>Look up via Bukkit ServicesManager:
 * <pre>{@code
 * ForgeAPI forge = Bukkit.getServicesManager().load(ForgeAPI.class);
 * if (forge != null) {
 *     int level = forge.getForgeLevel(item);
 * }
 * }</pre>
 */
public interface ForgeAPI extends YinwuAPI {

    /** Check if an item has been forged by YinwuForge. */
    boolean isForgeItem(ItemStack item);

    /** Get the item's total forge count. 0 if not forged. */
    int getForgeLevel(ItemStack item);

    /** Get the item's forge attack damage bonus. */
    int getBaseDamageBonus(ItemStack item);

    /** Get the item's forge armor value bonus. */
    int getArmorValueBonus(ItemStack item);

    /** Get the item's forge max durability bonus. */
    int getMaxDurabilityBonus(ItemStack item);

    /** Attempt to forge an item (simulate result without consuming materials). */
    ForgeSimulation simulate(ItemStack item);

    /** Result of a forge simulation. */
    record ForgeSimulation(
        String result,            // "success", "perfect", "downgrade", "destroy", "fail_no_penalty"
        int forgeCountAfter,
        String improvedAttribute, // null if no attribute changed
        int improveAmount         // 0 if no change
    ) {}
}

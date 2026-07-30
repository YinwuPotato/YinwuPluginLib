package net.yinwu.lib.api;

import net.yinwu.lib.plugin.YinwuAPI;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * YinwuEnchant plugin API.
 *
 * <p>Look up via Bukkit ServicesManager:
 * <pre>{@code
 * EnchantAPI enchant = Bukkit.getServicesManager().load(EnchantAPI.class);
 * if (enchant != null) {
 *     ItemStack book = enchant.createEnchantedBook("cats_paw", 3);
 * }
 * }</pre>
 */
public interface EnchantAPI extends YinwuAPI {

    /** Get all registered enchantment IDs. */
    List<String> getEnchantmentIds();

    /** Get the display name for an enchantment. */
    String getDisplayName(String enchantId);

    /** Get the max level for an enchantment. */
    int getMaxLevel(String enchantId);

    /** Create an enchanted book for the given enchantment. */
    ItemStack createEnchantedBook(String enchantId, int level);

    /** Apply an enchantment to an item and return it. */
    ItemStack applyEnchantment(ItemStack item, String enchantId, int level);

    /** Check if an item has a specific Yinwu enchantment. */
    boolean hasEnchantment(ItemStack item, String enchantId);

    /** Get the level of a specific enchantment on an item (0 if absent). */
    int getEnchantmentLevel(ItemStack item, String enchantId);

    /** Get all Yinwu enchantments on an item. */
    List<EnchantmentInstance> getEnchantments(ItemStack item);

    /** A single enchantment instance on an item. */
    record EnchantmentInstance(String id, String displayName, int level) {}
}

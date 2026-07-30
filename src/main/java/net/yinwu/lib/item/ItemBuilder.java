package net.yinwu.lib.item;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * Fluent ItemStack builder.
 *
 * <pre>{@code
 * ItemStack sword = ItemBuilder.of(Material.DIAMOND_SWORD)
 *     .name("&6Excalibur")
 *     .lore("&7The legendary sword")
 *     .enchant(Enchantment.SHARPNESS, 5)
 *     .amount(1)
 *     .build();
 * }</pre>
 */
public class ItemBuilder {

    private final ItemStack item;
    private ItemMeta meta;

    private ItemBuilder(Material material) {
        this.item = new ItemStack(material);
        this.meta = item.getItemMeta();
    }

    private ItemBuilder(ItemStack existing) {
        this.item = existing.clone();
        this.meta = item.getItemMeta();
    }

    /** Start building from a material. */
    public static ItemBuilder of(Material material) {
        return new ItemBuilder(material);
    }

    /** Start building from an existing ItemStack (clones it). */
    public static ItemBuilder from(ItemStack existing) {
        return new ItemBuilder(existing);
    }

    // ---- Meta access ----

    private ItemMeta meta() {
        if (meta == null) {
            meta = item.getItemMeta();
        }
        return meta;
    }

    private ItemBuilder editMeta(Consumer<ItemMeta> editor) {
        editor.accept(meta());
        return this;
    }

    // ---- Fluent API ----

    public ItemBuilder amount(int amount) {
        item.setAmount(amount);
        return this;
    }

    public ItemBuilder name(String legacyName) {
        return editMeta(m -> m.setDisplayName(color(legacyName)));
    }

    public ItemBuilder name(Component name) {
        return editMeta(m -> m.displayName(name));
    }

    public ItemBuilder lore(String... lines) {
        return lore(Arrays.asList(lines));
    }

    public ItemBuilder lore(List<String> lines) {
        return editMeta(m -> {
            List<String> colored = new ArrayList<>();
            for (String line : lines) {
                colored.add(color(line));
            }
            m.setLore(colored);
        });
    }

    public ItemBuilder lore(Component... components) {
        return editMeta(m -> m.lore(Arrays.asList(components)));
    }

    public ItemBuilder enchant(Enchantment ench, int level) {
        item.addUnsafeEnchantment(ench, level);
        return this;
    }

    public ItemBuilder flags(ItemFlag... flags) {
        return editMeta(m -> m.addItemFlags(flags));
    }

    public ItemBuilder unbreakable(boolean unbreakable) {
        return editMeta(m -> m.setUnbreakable(unbreakable));
    }

    public ItemBuilder customModel(int data) {
        return editMeta(m -> m.setCustomModelData(data));
    }

    /** Set a value in the PersistentDataContainer. */
    public <T, Z> ItemBuilder pdc(NamespacedKeyCache key, PersistentDataType<T, Z> type, Z value) {
        return editMeta(m -> m.getPersistentDataContainer().set(key.key(), type, value));
    }

    /** Remove a PDC value. */
    public <T, Z> ItemBuilder removePdc(NamespacedKeyCache key) {
        return editMeta(m -> m.getPersistentDataContainer().remove(key.key()));
    }

    // ---- Build ----

    public ItemStack build() {
        if (meta != null) {
            item.setItemMeta(meta);
        }
        return item;
    }

    // ---- PDC read helpers ----

    /** Read a PDC value from an existing item. */
    public static <T, Z> Z readPdc(ItemStack item, NamespacedKeyCache key, PersistentDataType<T, Z> type) {
        ItemMeta m = item.getItemMeta();
        if (m == null) return null;
        return m.getPersistentDataContainer().get(key.key(), type);
    }

    /** Check if an item has a PDC key. */
    public static boolean hasPdc(ItemStack item, NamespacedKeyCache key) {
        ItemMeta m = item.getItemMeta();
        if (m == null) return false;
        return m.getPersistentDataContainer().has(key.key());
    }

    // ---- Internal ----

    private static String color(String text) {
        return LegacyComponentSerializer.legacySection().serialize(
            LegacyComponentSerializer.legacyAmpersand().deserialize(text)
        );
    }
}

package net.yinwu.lib.gui;

import net.kyori.adventure.text.Component;
import net.yinwu.lib.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Base class for Yinwu plugin GUIs.
 *
 * <p>Handles concurrent player access, item return on close, and
 * click cancellation for non-slot areas.
 *
 * <p>Subclass must implement:
 * <ul>
 *   <li>{@link #title()} — GUI title</li>
 *   <li>{@link #size()} — inventory size (multiple of 9)</li>
 *   <li>{@link #layout(Inventory)} — populate slots</li>
 *   <li>{@link #handleSlot(Player, Inventory, int, InventoryClickEvent)} — slot clicks</li>
 * </ul>
 */
public abstract class AbstractGUI {

    protected final JavaPlugin plugin;
    protected final Map<UUID, Inventory> open = new ConcurrentHashMap<>();

    protected AbstractGUI(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /** GUI title shown at the top of the window. */
    public abstract String title();

    /** Inventory size (must be multiple of 9). */
    public abstract int size();

    /** Populate the inventory with items. */
    protected abstract void layout(Inventory inv);

    /** Handle a click on a GUI slot. Return true if handled. */
    protected abstract boolean handleSlot(Player player, Inventory inv, int slot, InventoryClickEvent event);

    /** Called when a GUI closes — override to do extra cleanup. */
    protected void onClose(Player player) {}

    // ---- Public API ----

    /** Open the GUI for a player. */
    public void open(Player player) {
        Inventory inv = Bukkit.createInventory(null, size(), Component.text(title()));
        layout(inv);
        player.openInventory(inv);
        open.put(player.getUniqueId(), inv);
    }

    /** Close the GUI for a player, returning items. */
    public void close(Player player) {
        Inventory inv = open.remove(player.getUniqueId());
        if (inv != null) {
            returnItems(player, inv);
        }
    }

    /** Close all open GUIs (called on plugin disable). */
    public void closeAll() {
        for (Map.Entry<UUID, Inventory> entry : open.entrySet()) {
            Player player = Bukkit.getPlayer(entry.getKey());
            if (player != null && player.isOnline()) {
                close(player);
            }
        }
        open.clear();
    }

    /** Check if a player has this GUI open. */
    public boolean isOpen(Player player) {
        return open.containsKey(player.getUniqueId());
    }

    /** Forward inventory clicks from the plugin's shared listener. */
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        Inventory inv = open.get(player.getUniqueId());
        if (inv == null) return;
        if (!event.getView().title().equals(Component.text(title()))) return;

        int slot = event.getRawSlot();
        boolean guiSlot = slot >= 0 && slot < size();

        if (guiSlot) {
            event.setCancelled(true);
            if (handleSlot(player, inv, slot, event)) return;
        }
    }

    /** Forward inventory close events. */
    public void onClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) return;
        if (!open.containsKey(player.getUniqueId())) return;
        close(player);
        onClose(player);
    }

    // ---- Helpers for subclasses ----

    /** Fill border slots with glass pane. */
    protected void fillBorder(Inventory inv, Material paneType) {
        ItemStack pane = ItemBuilder.of(paneType).name(" ").build();
        for (int i = 0; i < size(); i++) {
            if (inv.getItem(i) == null || inv.getItem(i).getType() == Material.AIR) {
                inv.setItem(i, pane);
            }
        }
    }

    /** Return items to player inventory (drop overflow). */
    protected void returnItems(Player player, Inventory inv) {
        for (int i = 0; i < size(); i++) {
            ItemStack item = inv.getItem(i);
            if (item != null && item.getType() != Material.AIR) {
                Map<Integer, ItemStack> leftover = player.getInventory().addItem(item);
                for (ItemStack left : leftover.values()) {
                    player.getWorld().dropItemNaturally(player.getLocation(), left);
                }
            }
        }
    }

    /** Consume one item from a stack (return null if last). */
    protected ItemStack consumeOne(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return null;
        if (item.getAmount() <= 1) return null;
        item.setAmount(item.getAmount() - 1);
        return item;
    }

    /** Get the open inventory for a player (null if not open). */
    protected Inventory getInventory(Player player) {
        return open.get(player.getUniqueId());
    }
}

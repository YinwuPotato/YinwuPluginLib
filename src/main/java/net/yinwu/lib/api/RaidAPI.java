package net.yinwu.lib.api;

import net.yinwu.lib.plugin.YinwuAPI;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * YinwuRaid plugin API.
 *
 * <p>Look up via Bukkit ServicesManager:
 * <pre>{@code
 * RaidAPI raid = Bukkit.getServicesManager().load(RaidAPI.class);
 * if (raid != null && raid.isInRaid(player)) {
 *     int level = raid.getRaidLevel(player);
 * }
 * }</pre>
 */
public interface RaidAPI extends YinwuAPI {

    /** Check if a player is currently in a raid. */
    boolean isInRaid(Player player);

    /** Get the player's current raid doom level (0 if not in raid). */
    int getRaidLevel(Player player);

    /** Get the current raid wave number (0 if not in raid). */
    int getCurrentWave(Player player);

    /** Get the total wave count for the current raid. */
    int getTotalWaves(Player player);

    /** Check if an entity is a YinwuRaid mob (elite/boss/raid mob). */
    boolean isRaidMob(Entity entity);

    /** Check if an entity is a raid boss. */
    boolean isRaidBoss(Entity entity);

    /** Get the names of all active raid participants. */
    List<String> getActiveRaidPlayers();

    /** Get items that are boosted against raid mobs (from forged gear). */
    boolean hasRaidBonus(ItemStack item);
}

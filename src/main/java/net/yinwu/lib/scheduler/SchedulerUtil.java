package net.yinwu.lib.scheduler;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import java.util.Objects;

/**
 * Folia scheduler utility using only the Folia scheduler API,
 * which also works on Paper (internally dispatched to the main thread).
 *
 * <p>Each plugin creates its own instance via {@link #init(Plugin)}.
 *
 * <p>Usage:
 * <pre>{@code
 * SchedulerUtil sch = SchedulerUtil.init(myPlugin);
 * sch.global(task);
 * sch.atEntity(player).runDelayed(task, 1L);
 * }</pre>
 */
public final class SchedulerUtil {

    private final Plugin plugin;

    private SchedulerUtil(Plugin plugin) {
        this.plugin = Objects.requireNonNull(plugin, "plugin");
    }

    /**
     * Create a scheduler instance for the given plugin.
     * Each plugin should call this once and cache the result.
     */
    public static SchedulerUtil init(Plugin plugin) {
        return new SchedulerUtil(plugin);
    }

    // ---- Instance methods (bound to the owning plugin) ----

    /** Run on the global region thread (safe for world-border, non-entity state). */
    public void global(Runnable task) {
        Bukkit.getGlobalRegionScheduler().execute(plugin, task);
    }

    /** Run globally with delay ≥ 1 tick. */
    public void globalLater(Runnable task, long delayTicks) {
        long d = Math.max(1L, delayTicks);
        Bukkit.getGlobalRegionScheduler().runDelayed(plugin, t -> task.run(), d);
    }

    /** Run globally at fixed rate. Delay ≥ 1 tick. */
    public Object globalTimer(Runnable task, long delayTicks, long periodTicks) {
        long d = Math.max(1L, delayTicks);
        return Bukkit.getGlobalRegionScheduler().runAtFixedRate(plugin, t -> task.run(), d, periodTicks);
    }

    /** Run at the entity's owning region thread. */
    public void atEntity(Entity entity, Runnable task) {
        entity.getScheduler().execute(plugin, task, null, 1L);
    }

    /** Run at entity with delay ≥ 1 tick. */
    public void atEntityLater(Entity entity, Runnable task, long delayTicks) {
        long d = Math.max(1L, delayTicks);
        entity.getScheduler().runDelayed(plugin, t -> task.run(), null, d);
    }

    /** Run at entity at fixed rate. Delay ≥ 1 tick. */
    public Object atEntityTimer(Entity entity, Runnable task, long delayTicks, long periodTicks) {
        long d = Math.max(1L, delayTicks);
        return entity.getScheduler().runAtFixedRate(plugin, t -> task.run(), null, d, periodTicks);
    }

    /** Run at the region owning the location. */
    public void atRegion(Location location, Runnable task) {
        Bukkit.getRegionScheduler().run(plugin, location, t -> task.run());
    }

    /** Run at the region owning the chunk. */
    public void atChunk(Chunk chunk, Runnable task) {
        if (chunk.getWorld() != null) {
            atRegion(chunk.getBlock(0, 0, 0).getLocation(), task);
        } else {
            global(task);
        }
    }

    /** Run async (never touch Bukkit API in these). */
    public void async(Runnable task) {
        Bukkit.getAsyncScheduler().runNow(plugin, t -> task.run());
    }

    // ---- Static utility methods (no plugin context needed) ----

    /** Cancel a recurring task returned by globalTimer / atEntityTimer. */
    public static void cancel(Object task) {
        if (task instanceof io.papermc.paper.threadedregions.scheduler.ScheduledTask st) {
            st.cancel();
        }
    }

    /** Cancel all tasks owned by the given plugin. */
    public static void cancelAll(Plugin p) {
        Bukkit.getGlobalRegionScheduler().cancelTasks(p);
    }

    // ---- Thread checks ----

    /** True if current thread is the owner of the given location's region. */
    public static boolean isOwnedByCurrentRegion(Location location) {
        return Bukkit.isOwnedByCurrentRegion(location);
    }

    /** True if current thread is the owner of the given entity's region. */
    public static boolean isOwnedByCurrentRegion(Entity entity) {
        return Bukkit.isOwnedByCurrentRegion(entity);
    }

    /** Throw if not in the correct region for the location. */
    public static void assertRegion(Location location, String operation) {
        if (!Bukkit.isOwnedByCurrentRegion(location)) {
            throw new IllegalStateException("Operation '" + operation + "' must run in the owning region thread");
        }
    }
}

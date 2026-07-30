package net.yinwu.lib.plugin;

import net.yinwu.lib.scheduler.SchedulerUtil;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Base class for all Yinwu plugins.
 * Provides common lifecycle management, Folia-aware scheduling, and debug logging.
 */
public abstract class YinwuPlugin extends JavaPlugin {

    private boolean debugEnabled;
    private boolean foliaDetected;
    private SchedulerUtil scheduler;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadConfig();

        detectFolia();
        debugEnabled = getConfig().getBoolean("debug", false);

        this.scheduler = SchedulerUtil.init(this);

        enable();
    }

    @Override
    public void onDisable() {
        disable();

        Bukkit.getGlobalRegionScheduler().cancelTasks(this);
        HandlerList.unregisterAll(this);

        if (debugEnabled) {
            getLogger().info(name() + " 已安全禁用");
        }
    }

    /**
     * Plugin-specific enable logic. Called after config is loaded and Folia is detected.
     */
    protected abstract void enable();

    /**
     * Plugin-specific disable logic. Called before task/listener cleanup.
     */
    protected abstract void disable();

    /**
     * Short plugin display name (for logging).
     */
    public abstract String name();

    // ---- Folia detection ----

    private void detectFolia() {
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            foliaDetected = true;
        } catch (ClassNotFoundException e) {
            foliaDetected = false;
        }
    }

    public boolean isFolia() {
        return foliaDetected;
    }

    // ---- Debug ----

    public boolean isDebug() {
        return debugEnabled;
    }

    public void debug(String message) {
        if (debugEnabled) {
            getLogger().info("[DEBUG] " + message);
        }
    }

    public void fine(String message) {
        getLogger().fine(message);
    }

    // ---- Convenience ----

    public SchedulerUtil scheduler() {
        return scheduler;
    }
}

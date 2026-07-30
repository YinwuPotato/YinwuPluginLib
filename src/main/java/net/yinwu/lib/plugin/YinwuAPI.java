package net.yinwu.lib.plugin;

/**
 * Marker interface for Yinwu plugin APIs.
 * Each Yinwu plugin that exposes cross-plugin functionality
 * should create an interface extending this and register it
 * via Bukkit's ServicesManager.
 *
 * <pre>{@code
 * // Register provider:
 * Bukkit.getServicesManager().register(ForgeAPI.class, impl, plugin, ServicePriority.Normal);
 *
 * // Lookup consumer:
 * ForgeAPI forge = Bukkit.getServicesManager().load(ForgeAPI.class);
 * }</pre>
 */
public interface YinwuAPI {
    /** Human-readable API version for compatibility checks. */
    String apiVersion();
}

package net.yinwu.lib.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Base implementation of {@link ConfigHolder} that caches config values
 * in a ConcurrentHashMap for thread-safe reads.
 *
 * <p>Extend this and call {@link #cache(String, Object)} for each key
 * in your {@link #reload()} implementation.
 *
 * <p>Example:
 * <pre>{@code
 * public class MyConfig extends BaseConfigManager {
 *     public MyConfig(JavaPlugin plugin) {
 *         super(plugin);
 *     }
 *
 *     @Override
 *     public void reload() {
 *         super.reload(); // calls plugin.reloadConfig()
 *         cache("foo", raw().getInt("foo", 42));
 *         cache("bar", raw().getString("bar", "default"));
 *     }
 *
 *     public int foo() { return getInt("foo"); }
 *     public String bar() { return getString("bar"); }
 * }
 * }</pre>
 */
public class BaseConfigManager implements ConfigHolder {

    protected final JavaPlugin plugin;
    private final Map<String, Object> cache = new ConcurrentHashMap<>();
    private FileConfiguration config;

    public BaseConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void reload() {
        plugin.reloadConfig();
        this.config = plugin.getConfig();
        cache.clear();
    }

    @Override
    public FileConfiguration raw() {
        if (config == null) {
            config = plugin.getConfig();
        }
        return config;
    }

    // ---- Cache helpers for subclasses ----

    /** Store a value in the cache. Call from reload(). */
    protected void cache(String path, Object value) {
        cache.put(path, value);
    }

    /** Remove a key from the cache. */
    protected void uncache(String path) {
        cache.remove(path);
    }

    /** Cast the cached value or fall back to raw config. */
    @SuppressWarnings("unchecked")
    private <T> T get(String path, T fallback) {
        Object v = cache.get(path);
        if (v != null) return (T) v;
        // raw fallback
        if (raw().contains(path)) {
            return (T) raw().get(path);
        }
        return fallback;
    }

    @Override
    public int getInt(String path) {
        return get(path, raw().getInt(path, 0));
    }

    @Override
    public double getDouble(String path) {
        return get(path, raw().getDouble(path, 0.0));
    }

    @Override
    public boolean getBoolean(String path) {
        return get(path, raw().getBoolean(path, false));
    }

    @Override
    public String getString(String path) {
        return get(path, raw().getString(path));
    }

    @Override
    public long getLong(String path) {
        return get(path, raw().getLong(path, 0L));
    }
}

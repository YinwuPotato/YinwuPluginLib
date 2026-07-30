package net.yinwu.lib.config;

import org.bukkit.configuration.file.FileConfiguration;

/**
 * Thread-safe config cache interface.
 *
 * <p>All Yinwu plugin config managers implement this, providing a uniform
 * access pattern for {@code getInt}, {@code getDouble}, {@code getString},
 * {@code getBoolean}, {@code getLong}, and {@code reload}.
 *
 * <p>The underlying cache uses ConcurrentHashMap to allow concurrent reads
 * from Folia region threads without locking.
 */
public interface ConfigHolder {

    /** Reload from disk. Implementations must clear caches and re-read. */
    void reload();

    /** Raw Bukkit config (use sparingly — prefer cached accessors). */
    FileConfiguration raw();

    // ---- Typed cached accessors ----

    int getInt(String path);

    default int getInt(String path, int fallback) {
        int v = getInt(path);
        // getInt returns 0 for missing; distinguish via raw config
        if (v == 0 && !raw().contains(path)) return fallback;
        return v;
    }

    double getDouble(String path);

    default double getDouble(String path, double fallback) {
        double v = getDouble(path);
        if (v == 0 && !raw().contains(path)) return fallback;
        return v;
    }

    boolean getBoolean(String path);

    default boolean getBoolean(String path, boolean fallback) {
        if (!raw().contains(path)) return fallback;
        return getBoolean(path);
    }

    String getString(String path);

    default String getString(String path, String fallback) {
        String v = getString(path);
        return v != null ? v : fallback;
    }

    long getLong(String path);

    default long getLong(String path, long fallback) {
        long v = getLong(path);
        if (v == 0L && !raw().contains(path)) return fallback;
        return v;
    }
}

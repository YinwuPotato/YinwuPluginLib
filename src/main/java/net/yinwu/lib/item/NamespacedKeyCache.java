package net.yinwu.lib.item;

import org.bukkit.NamespacedKey;

/**
 * Cached NamespacedKey to avoid repeated allocation.
 *
 * <p>Bukkit's {@link NamespacedKey} constructor is cheap but not free;
 * for keys used in hot paths (every item check), cache them:
 *
 * <pre>{@code
 * private static final NamespacedKeyCache FORGE_DATA = new NamespacedKeyCache(plugin, "forge_data");
 * }</pre>
 */
public class NamespacedKeyCache {

    private final NamespacedKey key;

    public NamespacedKeyCache(String namespace, String key) {
        this.key = new NamespacedKey(namespace, key);
    }

    public NamespacedKeyCache(org.bukkit.plugin.Plugin plugin, String key) {
        this.key = new NamespacedKey(plugin, key);
    }

    /** Get the underlying NamespacedKey. */
    public NamespacedKey key() {
        return key;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof NamespacedKeyCache nc && key.equals(nc.key);
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    @Override
    public String toString() {
        return key.toString();
    }
}

package net.yinwu.lib.thread;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

/**
 * Thread-safe collection factories and region-check utilities.
 *
 * <p>Use these factory methods instead of standard collections when
 * the data is accessed from multiple Folia region threads.
 *
 * <p>Examples:
 * <pre>{@code
 * Map<UUID, PlayerData> data = ThreadSafe.map();
 * Set<String> flags = ThreadSafe.set();
 * List<String> logs = ThreadSafe.list();
 * }</pre>
 */
public final class ThreadSafe {

    private ThreadSafe() {}

    // ---- Collections ----

    /** ConcurrentHashMap-based Map, safe for cross-region access. */
    public static <K, V> Map<K, V> map() {
        return new ConcurrentHashMap<>();
    }

    /** ConcurrentHashMap.newKeySet(), safe for cross-region iteration. */
    public static <E> Set<E> set() {
        return ConcurrentHashMap.newKeySet();
    }

    /** CopyOnWriteArrayList, safe for iteration under concurrent modification. */
    public static <E> java.util.List<E> list() {
        return new CopyOnWriteArrayList<>();
    }

    /** AtomicInteger counter. */
    public static AtomicInteger counter(int initial) {
        return new AtomicInteger(initial);
    }

    /** AtomicLong counter. */
    public static AtomicLong counter(long initial) {
        return new AtomicLong(initial);
    }

    /** Atomic reference (volatile replacement for cross-thread reads). */
    public static <V> AtomicReference<V> ref(V initial) {
        return new AtomicReference<>(initial);
    }

    // ---- Thread-state helpers ----

    /** True if running on the region that owns the given location. */
    public static boolean inRegion(Location location) {
        return Bukkit.isOwnedByCurrentRegion(location);
    }

    /** True if running on the region that owns the given entity. */
    public static boolean inRegion(Entity entity) {
        return Bukkit.isOwnedByCurrentRegion(entity);
    }

    /** Assert we are in the correct region for the location. */
    public static void requireRegion(Location loc, String operation) {
        if (!inRegion(loc)) {
            throw new IllegalStateException("'" + operation + "' must run in the owning region thread");
        }
    }

    /** Assert we are in the correct region for the entity. */
    public static void requireRegion(Entity entity, String operation) {
        if (!inRegion(entity)) {
            throw new IllegalStateException("'" + operation + "' must run in the owning region thread");
        }
    }

    // ---- Safe if-absent helpers ----

    /**
     * Get or compute a value from the map atomically.
     * Equivalent to {@code map.computeIfAbsent(key, supplier)}.
     */
    public static <K, V> V computeIfAbsent(Map<K, V> map, K key, Supplier<V> supplier) {
        return map.computeIfAbsent(key, k -> supplier.get());
    }
}

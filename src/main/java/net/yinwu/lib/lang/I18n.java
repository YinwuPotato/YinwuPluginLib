package net.yinwu.lib.lang;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Minimal i18n loader.
 *
 * <p>Loads {@code lang/<locale>.yml} files from the plugin's resource directory.
 * Falls back to the embedded default file when a key is missing.
 *
 * <pre>{@code
 * // In onEnable():
 * I18n i18n = new I18n(plugin, "zh_CN");
 *
 * // Usage:
 * String msg = i18n.get("forge.success");
 * String withArgs = i18n.get("forge.damage", player.getName(), 42);
 * }</pre>
 */
public class I18n {

    private final JavaPlugin plugin;
    private final String locale;
    private final Map<String, String> cache = new ConcurrentHashMap<>();
    private YamlConfiguration fallback;

    /** Default locale key. */
    public static final String DEFAULT_LOCALE = "zh_CN";

    public I18n(JavaPlugin plugin, String locale) {
        this.plugin = plugin;
        this.locale = locale != null ? locale : DEFAULT_LOCALE;
        load();
    }

    /** Reload language files. */
    public final void load() {
        cache.clear();
        loadFallback();

        File langDir = new File(plugin.getDataFolder(), "lang");
        File langFile = new File(langDir, locale + ".yml");

        if (!langFile.exists()) {
            plugin.saveResource("lang/" + locale + ".yml", false);
        }

        if (langFile.exists()) {
            YamlConfiguration lang = YamlConfiguration.loadConfiguration(langFile);
            for (String key : lang.getKeys(true)) {
                String value = lang.getString(key);
                if (value != null) {
                    cache.put(key, value);
                }
            }
        }
    }

    /** Get a translated string, with {0}, {1}, … replacement. */
    public String get(String key, Object... args) {
        String template = cache.get(key);
        if (template == null && fallback != null) {
            template = fallback.getString(key);
        }
        if (template == null) {
            return "{" + key + "}";
        }
        return format(template, args);
    }

    /** Check if a key exists in the active locale. */
    public boolean has(String key) {
        return cache.containsKey(key) || (fallback != null && fallback.contains(key));
    }

    /** Current locale code. */
    public String locale() {
        return locale;
    }

    // ---- Internal ----

    private void loadFallback() {
        InputStream in = plugin.getResource("lang/" + DEFAULT_LOCALE + ".yml");
        if (in != null) {
            fallback = YamlConfiguration.loadConfiguration(new InputStreamReader(in, StandardCharsets.UTF_8));
        }
    }

    private static String format(String template, Object... args) {
        if (args.length == 0) return template;
        String result = template;
        for (int i = 0; i < args.length; i++) {
            result = result.replace("{" + i + "}", String.valueOf(args[i]));
        }
        return result;
    }
}

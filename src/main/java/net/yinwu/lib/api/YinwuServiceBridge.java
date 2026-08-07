package net.yinwu.lib.api;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

/**
 * 跨插件服务反射桥。
 *
 * 各 Yinwu 插件各自 shade YinwuPluginLib，同一接口类由不同 ClassLoader 加载，
 * Bukkit ServicesManager 按 Class 精确匹配会失效。本类从目标插件的 ClassLoader
 * 加载接口类，再取注册的服务实例，规避类加载隔离。
 */
public final class YinwuServiceBridge {

    private YinwuServiceBridge() {}

    /**
     * 从目标插件加载接口类并返回 ServicesManager 注册的 provider。
     *
     * @param pluginName   目标插件名（如 "YinwuEnchant"）
     * @param interfaceFqcn 接口全限定名（如 "net.yinwu.lib.api.EnchantAPI"）
     * @return provider 实例；插件未加载或未注册时返回 null
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Object getProvider(String pluginName, String interfaceFqcn) {
        try {
            Plugin target = Bukkit.getPluginManager().getPlugin(pluginName);
            if (target == null) return null;
            ClassLoader cl = target.getClass().getClassLoader();
            Class<?> apiClass = Class.forName(interfaceFqcn, true, cl);
            var reg = Bukkit.getServicesManager().getRegistration((Class) apiClass);
            return reg != null ? reg.getProvider() : null;
        } catch (Exception e) {
            return null;
        }
    }
}

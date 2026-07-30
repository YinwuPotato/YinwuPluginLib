# YinwuPluginLib

Version: **1.0.1**

Yinwu 插件集共享库，提供所有子模块的基础设施。

## 功能

- **YinwuPlugin** — 模板方法基类，封装 Folia 检测、配置加载、生命周期
- **SchedulerUtil** — Folia 调度器封装（Global/Region/Entity/Async Scheduler）
- **API 接口** — EnchantAPI / ForgeAPI / RaidAPI，通过 Bukkit ServicesManager 跨模块调用
- **AbstractGUI** — GUI 框架模板，自动管理打开玩家、点击转发
- **ItemBuilder** — 流式 ItemStack 构建器
- **I18n** — 多语言支持（lang/*.yml）
- **BaseConfigManager** — 线程安全配置管理器（ConcurrentHashMap 缓存）
- **NamespacedKeyCache** — 热路径 NamespacedKey 缓存
- **ThreadSafe** — 线程安全集合工厂

## 下载

[YinwuPluginLib-1.0.1.jar](https://github.com/qumingjam/YinwuPluginLib/releases/download/v1.0.1/YinwuPluginLib-1.0.1.jar)

## 技术栈

Java 21, Paper API 1.21+, Folia 兼容
包名：net.yinwu.lib

# YinwuPluginLib

Version: 1.0.1

Shared library for all Yinwu plugins. Provides base classes, scheduling, API interfaces, and utilities.

## Features

- **YinwuPlugin** — Template method base class, wraps Folia detection, config loading, lifecycle
- **SchedulerUtil** — Folia scheduler wrapper (Global/Region/Entity/Async Scheduler)
- **API Interfaces** — EnchantAPI / ForgeAPI / RaidAPI via Bukkit ServicesManager
- **AbstractGUI** — GUI framework with automatic player tracking
- **ItemBuilder** — Fluent ItemStack builder
- **I18n** — Multi-language support from lang/*.yml
- **BaseConfigManager** — Thread-safe config cache (ConcurrentHashMap)
- **NamespacedKeyCache** — Hot-path NamespacedKey cache
- **ThreadSafe** — Thread-safe collection factories

## Tech

Java 21, Paper API 1.21+, Folia compatible
Package: net.yinwu.lib

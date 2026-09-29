# YinwuPluginLib — Yinwu插件库
# YinwuPluginLib — Shared Library

**最新版本：v1.0.3** | [下载 Release](https://github.com/YinwuPotato/YinwuPluginLib/releases/tag/v1.0.3)

Shared library providing base classes, scheduling, API interfaces, and utilities for all Yinwu plugins.

Yinwu 插件集共享库，提供所有子模块的基础设施：基类、调度器、API接口、工具集合。

> ⚡ 完全兼容 Folia 区域线程调度，零 NMS。

---

## Features | 功能

| 模块 | 说明 |
|------|------|
| 🏗️ **YinwuPlugin** | 模板方法基类，封装 Folia 检测、配置加载、生命周期 |
| ⏰ **SchedulerUtil** | Folia 调度器封装（Global/Region/Entity/Async） |
| 🔌 **API 接口** | EnchantAPI / ForgeAPI / RaidAPI，通过 ServicesManager 调用；跨插件联动走 YinwuServiceBridge 反射桥 |
| 🖥️ **AbstractGUI** | GUI 框架模板，自动玩家跟踪、点击转发 |
| 🛠️ **ItemBuilder** | 流式 ItemStack 构建器 |
| 🌐 **I18n** | 多语言支持（lang/*.yml） |
| ⚙️ **BaseConfigManager** | 线程安全配置管理器（ConcurrentHashMap 缓存） |
| 🏷️ **NamespacedKeyCache** | 热路径 NamespacedKey 缓存 |
| 🧵 **ThreadSafe** | 线程安全集合工厂 |

---

## Architecture | 架构

```
net.yinwu.lib
├── api/              # 跨模块 API 接口
│   ├── EnchantAPI            # 附魔系统接口
│   ├── ForgeAPI              # 锻造系统接口
│   ├── RaidAPI               # 袭击系统接口
│   └── YinwuServiceBridge    # 跨插件服务反射桥
├── config/           # 配置管理器
│   ├── BaseConfigManager
│   └── ConfigHolder
├── gui/              # GUI 框架
│   └── AbstractGUI
├── item/             # 物品工具
│   ├── ItemBuilder
│   └── NamespacedKeyCache
├── lang/             # 多语言
│   └── I18n
├── plugin/           # 插件基类
│   ├── YinwuPlugin
│   └── YinwuAPI
├── scheduler/        # 调度封装
│   └── SchedulerUtil
└── thread/           # 线程安全工具
    └── ThreadSafe
```

---

## Build | 构建

```bash
git clone https://github.com/YinwuPotato/YinwuPluginLib.git
cd YinwuPluginLib
mvn clean package
```

产出：`target/YinwuPluginLib-1.0.3.jar`

---

## Dependencies | 依赖

- **Paper API 1.21.8**（provided）

---

## Design Principles | 设计原则

- **Folia First** — 调度和线程安全从设计之初即考虑
- **零 NMS** — 纯 Paper/Folia API
- **Java 21** — records、switch expressions、pattern matching

---

## Links | 链接

- 仓库：[github.com/YinwuPotato/YinwuPluginLib](https://github.com/YinwuPotato/YinwuPluginLib)
- 关联模块：YinwuForge | YinwuRaid | YinwuEnchant
- 作者：Qumingjam

# How It Works: A Technical Deep Dive

This document provides a detailed explanation of the internal architecture and mechanics of the ScreenshotBlocker library.

## Core Components

The library is built around three main components that work together to provide its functionality.

### 1. `ScreenshotBlockerManager`

This is the engine of the library. It is a standard Kotlin class that contains all the core logic for managing screenshot blocking. Its responsibilities include:
- **State Management**: Tracking the global settings (`globalEnable`, `debugMode`), the active policy, and any manual per-activity overrides.
- **Lifecycle Observation**: Attaching to the application's activity lifecycle to monitor when activities are created, resumed, and destroyed.
- **Applying Security Flags**: Directly adding or removing the `WindowManager.LayoutParams.FLAG_SECURE` from an activity's window based on the current rules.

The decision to place the core logic in this class makes the library flexible and testable, allowing it to be managed by Dependency Injection (DI) frameworks if needed.

### 2. `ScreenshotBlocker`

This is the public-facing `object` that acts as the primary entry point for most developers. It serves two main purposes:
- **Singleton Access**: It provides a globally accessible Singleton instance of the `ScreenshotBlockerManager`.
- **Convenience Wrapper**: It exposes all the public methods of the `ScreenshotBlockerManager` instance, allowing for a simple and clean API (e.g., `ScreenshotBlocker.init(...)`).

This dual-component design provides the best of both worlds: easy integration for simple use cases and DI-friendliness for complex, test-driven applications.

### 3. `WindowSecurePolicy`

This is an interface that defines the **Strategy pattern** for the library. It decouples the decision-making logic from the core `ScreenshotBlockerManager`. Implementations of this interface determine *whether* the security flag should be applied to a given activity. The library includes several pre-built policies (`AlwaysSecurePolicy`, `ConditionalSecurePolicy`, `AnnotationBasedSecurePolicy`, etc.) and allows developers to provide their own custom implementations.

## Architectural Design & Patterns

The library's architecture is designed to be robust, efficient, and maintainable.

- **Observer Pattern**: The library uses `Application.ActivityLifecycleCallbacks` to observe the state of all activities in the application. This allows the `ScreenshotBlockerManager` to automatically apply security policies as activities are created and destroyed without requiring any manual calls from the activities themselves.

- **Strategy Pattern**: As mentioned above, the `WindowSecurePolicy` interface allows the algorithm for applying the security flag to be swapped out at runtime. This is a powerful feature that makes the library highly extensible.

- **DI-Friendly Singleton**: By separating the manager from the singleton wrapper, the architecture supports modern Android development practices. Developers using Hilt, Koin, or other DI frameworks can inject `ScreenshotBlockerManager` directly, allowing for easier testing and lifecycle management.

## Execution Flow

1.  **Initialization**:
    - A developer calls `ScreenshotBlocker.init(application, ...)`.
    - The `ScreenshotBlocker` object gets or creates a singleton instance of `ScreenshotBlockerManager`.
    - The manager's `init()` method is called, storing the configuration (global settings, policy).
    - If it's the first time initializing, it registers the `ActivityLifecycleCallbacks`.

2.  **Activity Lifecycle**:
    - **`onActivityCreated`**: The activity is added to a `WeakHashMap` of active activities. The manager then immediately evaluates the current policy to decide if `FLAG_SECURE` should be applied.
    - **`onActivityResumed`**: The policy is re-evaluated to account for any configuration changes that may have occurred while the activity was paused.
    - **`onActivityDestroyed`**: The activity is removed from all internal tracking maps (`activeActivities`, `manualOverrides`) to prevent memory leaks.

3.  **Applying Security (`applyPolicyOrGlobalFlag`)**:
    When deciding whether to secure an activity, the manager follows a strict order of precedence:
    1.  **Debug Mode**: If `debugMode` is `true`, security is **always disabled**.
    2.  **Manual Override**: If `enableFor(activity)` or `disableFor(activity)` has been called for that specific activity, that setting is honored above all others.
    3.  **WindowSecurePolicy**: If a custom policy has been set, its `shouldApplyFlagSecure(activity)` method is called.
    4.  **Global Setting**: If no manual override or policy applies, the `globalEnable` setting is used as the fallback.

## Resource & Memory Management

The library uses `WeakHashMap` to store references to `Activity` objects. This is a critical design choice that prevents memory leaks. A `WeakHashMap` does not hold strong references to its keys. When an activity is destroyed and is no longer referenced anywhere else, the garbage collector can reclaim its memory, and its entry will be automatically removed from the map.

## Thread Safety

All public configuration methods and the internal `applyPolicyOrGlobalFlag` method are thread-safe. The core logic for applying the flag is `@Synchronized` to prevent race conditions that could occur if, for example, `setPolicy()` is called from a background thread at the same time an activity is being created on the main thread.

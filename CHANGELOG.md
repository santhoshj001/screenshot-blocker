# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.0] - 2024-06-20

### Added
- Initial release of Screenshot Blocker Android Library
- Global screenshot blocking functionality using `Application.ActivityLifecycleCallbacks`
- Per-activity screenshot control with `enableFor()` and `disableFor()` methods
- `ActivityLifecycleCallbacksAdapter` utility class for easier lifecycle management
- Comprehensive unit tests and instrumented tests
- Maven Central publishing configuration
- Complete documentation with README.md
- Apache 2.0 License
- Contributing guidelines
- ProGuard consumer rules for library integration

### Features
- ✅ **Global Screenshot Blocking**: Automatically protect all activities in your app
- ✅ **Per-Activity Control**: Fine-grained control over specific screens
- ✅ **Zero Dependencies**: Lightweight library with no third-party dependencies
- ✅ **Safe & Reliable**: Uses only standard Android APIs, no reflection or hacks
- ✅ **Easy Integration**: Simple one-line initialization
- ✅ **Compose Compatible**: Works with both View-based and Jetpack Compose apps
- ✅ **Modern Android**: Supports API 21+ (Android 5.0 Lollipop and above)

### Technical Details
- **Minimum SDK**: API 21 (Android 5.0 Lollipop)
- **Target SDK**: API 35 (Android 15)
- **Language**: Kotlin
- **Build Tool**: Gradle with Kotlin DSL
- **Testing**: JUnit 4, Mockito, Android Test Framework

### API Reference

#### ScreenshotBlocker
- `init(application: Application, enableGlobally: Boolean = true)` - Initialize the library
- `enableFor(activity: Activity)` - Enable screenshot blocking for specific activity
- `disableFor(activity: Activity)` - Disable screenshot blocking for specific activity
- `isGloballyEnabled(): Boolean` - Check if global blocking is enabled
- `isInitialized(): Boolean` - Check if library is initialized

#### ActivityLifecycleCallbacksAdapter
- Base adapter class for `Application.ActivityLifecycleCallbacks`
- Provides empty implementations for all lifecycle methods
- Allows extending only the methods you need

### Package Info
- **Group ID**: `io.github.sjdroid`
- **Artifact ID**: `screenshot-blocker`
- **Version**: `1.0.0`

---

## Unreleased

### Planned Features
- Fragment-level screenshot blocking
- Jetpack Compose-specific helpers
- Annotation-based configuration (@SecureScreen)
- Debug mode helpers for testing
- Enhanced error handling and logging options

---

## Version History

| Version | Release Date | Key Features |
|---------|-------------|--------------|
| 1.0.0   | 2024-06-20  | Initial release with core functionality |

---

## Migration Guide

### From Direct FLAG_SECURE Usage

If you're currently using `WindowManager.LayoutParams.FLAG_SECURE` directly:

```kotlin
// Before
window.setFlags(
    WindowManager.LayoutParams.FLAG_SECURE,
    WindowManager.LayoutParams.FLAG_SECURE
)

// After
ScreenshotBlocker.enableFor(this)
```

### From Other Screenshot Blocking Libraries

This library provides a simpler, more reliable approach:

1. Remove your existing screenshot blocking dependency
2. Add Screenshot Blocker: `implementation("io.github.sjdroid:screenshot-blocker:1.0.0")`
3. Initialize in your Application class: `ScreenshotBlocker.init(this)`
4. Remove any manual FLAG_SECURE implementations

---

## Support

- **Documentation**: [README.md](README.md)
- **Issues**: [GitHub Issues](https://github.com/sjdroid/screenshot-blocker/issues)
- **Discussions**: [GitHub Discussions](https://github.com/sjdroid/screenshot-blocker/discussions)
- **Contributing**: [CONTRIBUTING.md](CONTRIBUTING.md) 
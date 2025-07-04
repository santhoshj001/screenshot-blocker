# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.1.3] - 2025-06-28

### Fixed
- **Memory Leak**: Replaced `activity.hashCode()` with `WeakHashMap<Activity, Boolean>` for robust activity tracking, preventing memory leaks and ensuring correct flag application across lifecycle changes.
- **`init` Method Idempotency**: Modified `init` to be re-callable, allowing dynamic updates to `enableGlobally`, `debugMode`, and `policy` after initial setup.
- **Silent Exception Handling**: Improved error handling in `applySecureFlag` to log exceptions, aiding in debugging.

### Added
- **Policy-Based Security**: Introduced `WindowSecurePolicy` interface and concrete implementations (`AlwaysSecurePolicy`, `NeverSecurePolicy`, `ConditionalSecurePolicy`, `AnnotationBasedSecurePolicy`, `AndSecurePolicy`, `OrSecurePolicy`) for fine-grained control over screenshot blocking.
- **`@SecureScreen` Annotation**: Added annotation for use with `AnnotationBasedSecurePolicy`.
- **Runtime Introspection APIs**: Implemented `isDebugMode()`, `getSecuredActivitiesCount()`, `getPolicy()`, and `setPolicy()` for programmatic access to library state and policy management.
- **`ActivityLifecycleCallbacksAdapter`**: Added utility class as mentioned in documentation.

### Enhanced
- **Activity Tracking**: Significantly improved reliability of activity tracking across lifecycle events.
- **Configuration Flexibility**: `init` method now supports dynamic configuration updates.
- **Debugging**: Enhanced error logging for easier troubleshooting.
- **Test Coverage**: Expanded unit and instrumented tests to cover new APIs, policy-based logic, and activity lifecycle scenarios.

## [1.1.2] - 2024-12-22

### Fixed
- **fix(sample):** Add missing `material-icons-extended` dependency to resolve compilation failure.
- **fix(sample):** Correctly reference `Icons.Outlined.Shield` to fix icon-related build error.

## [1.1.1] - 2024-12-22

### Fixed
- 🔧 **CI/CD Pipeline Issues** - Fixed missing gradle-wrapper.properties causing build failures
- 🧩 **Kotlin 2.0 Compatibility** - Updated to kotlin-compose plugin, removed deprecated composeOptions
- 🧪 **Unit Test Stability** - Fixed Mockito stubbing issues in WindowSecurePolicyTest with real Activity classes
- 📱 **Android Instrumentation Tests** - Completely overhauled tests to be CI/CD compatible:
  - Fixed package name assertion (com.sjdroid.screenshotblocker.test)
  - Eliminated singleton state conflicts between tests
  - Removed emulator-dependent assertions
  - Made tests resilient to initialization order
- ⚙️ **Build Configuration** - Added BuildConfig generation, fixed compilation errors

### Enhanced
- **Test Infrastructure** - More robust and reliable test suite for CI/CD environments
- **Compose Compiler** - Updated to use new Compose Compiler Gradle plugin architecture
- **CI/CD Reliability** - All build issues resolved, pipeline now runs successfully
- **Documentation** - Updated version references and installation instructions

### Technical
- **Gradle Wrapper** - Fixed .gitignore to properly include gradle-wrapper.properties
- **Kotlin Compiler** - Updated to Kotlin 2.0.21 with proper Compose support
- **Test Framework** - Improved test isolation and state management
- **Build System** - Enhanced configuration cache compatibility

## [1.1.0] - 2024-12-21

### Added
- 🎯 **Lifecycle-aware behavior** - Enhanced activity lifecycle handling with automatic cleanup
- 🧩 **Jetpack Compose support** - `SecureScreen` composable and `Modifier.secure()` extension
- 🐛 **Debug override** - Automatic security disabling in debug builds via `BuildConfig.DEBUG` 
- 🔍 **Runtime introspection APIs** - New methods: `isSecureEnabled()`, `isDebugMode()`, `getSecuredActivitiesCount()`
- 📋 **WindowSecurePolicy interface** - Fine-grained control with custom security policies:
  - `AlwaysSecurePolicy` - Secure all activities
  - `NeverSecurePolicy` - Never secure activities (useful for testing)
  - `ConditionalSecurePolicy` - Secure specific activities by class name
  - `AnnotationBasedSecurePolicy` - Secure activities annotated with `@SecureScreen`
  - `AndSecurePolicy` / `OrSecurePolicy` - Combine multiple policies with logical operators
- 🔧 **Enhanced initialization** - New optional parameters for debug mode and custom policies
- ☕ **Better Java interoperability** - Added `@JvmStatic` and `@JvmOverloads` annotations
- 🚀 **CI/CD pipeline** - GitHub Actions workflow with automated testing and quality checks
- 📊 **Code quality tools** - Integrated lint checks and detekt for better code quality
- 🎨 **Enhanced sample app** - Modern Compose UI demonstrating all library features

### Enhanced
- **ScreenshotBlocker.init()** - Now supports `debugMode` and `policy` parameters
- **Activity tracking** - Improved memory management and automatic cleanup on activity destruction
- **Error handling** - More robust exception handling for edge cases and activity lifecycle
- **Documentation** - Comprehensive updates with usage examples and platform limitations

### Security
- **Policy-based security** - Flexible, configurable control over when FLAG_SECURE is applied
- **Debug mode safety** - Prevents accidental security bypass in production builds
- **Memory management** - Better tracking and cleanup of secured activities

## [1.0.0] - 2024-06-20

### Added
- Initial release of Screenshot Blocker Android Library
- Global screenshot blocking functionality using `Application.ActivityLifecycleCallbacks`
- Per-activity screenshot control with `enableFor()` and `disableFor()` methods
- `ActivityLifecycleCallbacksAdapter` utility class for easier lifecycle management
- Comprehensive unit tests and instrumented tests
- JitPack distribution setup
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
- **Repository**: `com.github.santhoshj001:screenshot-blocker`
- **Version**: `v1.0.0`

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
2. Add JitPack repository and Screenshot Blocker dependency
3. Initialize in your Application class: `ScreenshotBlocker.init(this)`
4. Remove any manual FLAG_SECURE implementations

---

## Support

- **Documentation**: [README.md](README.md)
- **Issues**: [GitHub Issues](https://github.com/sjdroid/screenshot-blocker/issues)
- **Discussions**: [GitHub Discussions](https://github.com/sjdroid/screenshot-blocker/discussions)
- **Contributing**: [CONTRIBUTING.md](CONTRIBUTING.md) 
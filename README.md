# 🛡️ Screenshot Blocker

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![JitPack](https://jitpack.io/v/santhoshj001/screenshot-blocker.svg)](https://jitpack.io/#santhoshj001/screenshot-blocker)
[![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=21)
[![Version](https://img.shields.io/badge/Latest-v1.1.1-success.svg)](https://github.com/santhoshj001/screenshot-blocker/releases/tag/v1.1.1)
[![CI/CD](https://github.com/santhoshj001/screenshot-blocker/actions/workflows/ci.yml/badge.svg)](https://github.com/santhoshj001/screenshot-blocker/actions/workflows/ci.yml)

A comprehensive and production-ready Android library that prevents screenshots and screen recording using Android's `FLAG_SECURE` feature. Built with Kotlin, zero dependencies, and designed for modern Android applications.

## ✨ Features

- 🔒 **Global & Per-Activity Control** - Protect entire app or specific screens
- 🎯 **Lifecycle-Aware** - Automatically handles activity lifecycle events  
- 🧩 **Jetpack Compose Support** - Native composables and modifiers
- 📋 **Policy-Based Control** - Fine-grained control with custom policies
- 🐛 **Debug Override** - Automatically disable in debug builds
- 🔍 **Runtime Introspection** - Check security status programmatically
- ☕ **Java Friendly** - Full Java interoperability
- 📱 **API 21+** - Supports Android 5.0 and above
- 🚀 **Zero Dependencies** - Uses only standard Android APIs
- 🧪 **Thoroughly Tested** - Unit and instrumentation tests

## 📦 Installation

### Kotlin DSL
```kotlin
// In your root build.gradle.kts
repositories {
    maven { url = uri("https://jitpack.io") }
}

// In your app build.gradle.kts
dependencies {
    implementation("com.github.santhoshj001:screenshot-blocker:v1.1.1")
}
```

### Groovy DSL
```groovy
// In your root build.gradle
repositories {
    maven { url 'https://jitpack.io' }
}

// In your app build.gradle
dependencies {
    implementation 'com.github.santhoshj001:screenshot-blocker:v1.1.1'
}
```

## 🚀 Quick Start

### 1. Global Screenshot Blocking

Initialize in your `Application` class to automatically protect all activities:

```kotlin
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ScreenshotBlocker.init(this) // Enables global screenshot blocking
    }
}
```

Don't forget to register your Application class in `AndroidManifest.xml`:

```xml
<application
    android:name=".MyApplication"
    ... >
</application>
```

### 2. Per-Activity Control

For fine-grained control over specific activities:

```kotlin
class SecureActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Enable screenshot blocking for this activity only
        ScreenshotBlocker.enableFor(this)
        
        setContentView(R.layout.activity_secure)
    }
}
```

### 3. Dynamic Control

Enable or disable screenshot blocking at runtime:

```kotlin
// In your activity or fragment
fun toggleScreenshotBlocking(enable: Boolean) {
    if (enable) {
        ScreenshotBlocker.enableFor(this)
    } else {
        ScreenshotBlocker.disableFor(this)
    }
}
```

## 🔧 Advanced Usage

### Conditional Initialization

```kotlin
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Only enable in production builds
        val enableScreenshotBlocking = !BuildConfig.DEBUG
        ScreenshotBlocker.init(this, enableScreenshotBlocking)
    }
}
```

### Checking Library State

```kotlin
// Check if the library is initialized
if (ScreenshotBlocker.isInitialized()) {
    // Library is ready to use
}

// Check if global blocking is enabled
if (ScreenshotBlocker.isGloballyEnabled()) {
    // Global screenshot blocking is active
}
```

### Jetpack Compose Integration

Works seamlessly with Compose activities:

```kotlin
class ComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Enable screenshot blocking
        ScreenshotBlocker.enableFor(this)
        
        setContent {
            MyComposeTheme {
                // Your Compose UI
                MainScreen()
            }
        }
    }
}
```

## 🏗️ How It Works

The library uses Android's `WindowManager.LayoutParams.FLAG_SECURE` feature, which:

- ✅ Prevents screenshots via system screenshot methods
- ✅ Blocks screen recording of the protected content
- ✅ Hides app content in recent apps overview
- ✅ Prevents content capture by accessibility services
- ✅ Works across all Android versions (API 21+)

### Technical Implementation

1. **Global Mode**: Uses `Application.ActivityLifecycleCallbacks` to automatically apply `FLAG_SECURE` to all activities
2. **Manual Mode**: Provides direct API methods to control the flag on specific activities
3. **Exception Handling**: Gracefully handles edge cases like finishing activities

## 📱 Compatibility

- **Android API**: 21+ (Android 5.0 Lollipop and above)
- **Kotlin**: 1.8.0+
- **Java**: Compatible with Java projects
- **Architecture**: Works with MVVM, MVP, MVI, and other patterns
- **UI Frameworks**: View-based, Jetpack Compose, Hybrid apps

## ⚠️ Important Considerations

### Security Limitations

While `FLAG_SECURE` provides good protection against casual screenshot attempts, be aware:

- 🔒 **Rooted devices**: May bypass this protection
- 📱 **Physical photography**: Cannot prevent photos of the screen
- 🔧 **Advanced tools**: Specialized screen capture tools might circumvent this
- 🐛 **OS bugs**: Some device manufacturers may have implementation issues

### Best Practices

1. **Use for sensitive content**: Ideal for login screens, payment pages, personal information
2. **Combine with other security measures**: Use alongside encryption, certificate pinning, etc.
3. **Test thoroughly**: Verify behavior across different devices and Android versions
4. **Consider UX impact**: Screenshots are disabled in recent apps overview

## 🧪 Testing

The library includes comprehensive unit tests. To run them:

```bash
./gradlew test
```

For testing in your app:

```kotlin
// Test mode - disable screenshot blocking for UI tests
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Don't block screenshots during UI testing
        val enableBlocking = !isRunningTests()
        ScreenshotBlocker.init(this, enableBlocking)
    }
    
    private fun isRunningTests(): Boolean {
        return try {
            Class.forName("androidx.test.espresso.Espresso")
            true
        } catch (e: ClassNotFoundException) {
            false
        }
    }
}
```

## 📚 API Reference

### ScreenshotBlocker

| Method | Description |
|--------|-------------|
| `init(application: Application, enableGlobally: Boolean = true)` | Initialize the library with global settings |
| `enableFor(activity: Activity)` | Enable screenshot blocking for specific activity |
| `disableFor(activity: Activity)` | Disable screenshot blocking for specific activity |
| `isGloballyEnabled(): Boolean` | Check if global blocking is enabled |
| `isInitialized(): Boolean` | Check if library is initialized |

### ActivityLifecycleCallbacksAdapter

A utility class that provides default implementations for `Application.ActivityLifecycleCallbacks`. Extend this class if you need to implement your own lifecycle callbacks while using only specific methods.

## 🔄 Migration Guide

### From Direct FLAG_SECURE Usage

If you're currently using `FLAG_SECURE` directly:

```kotlin
// Before
window.setFlags(
    WindowManager.LayoutParams.FLAG_SECURE,
    WindowManager.LayoutParams.FLAG_SECURE
)

// After
ScreenshotBlocker.enableFor(this)
```

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request. For major changes, please open an issue first to discuss what you would like to change.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

```
Copyright 2024 Santhosh J.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

## 📞 Support

- 🐛 **Bug Reports**: [GitHub Issues](https://github.com/sjdroid/screenshot-blocker/issues)
- 💡 **Feature Requests**: [GitHub Discussions](https://github.com/sjdroid/screenshot-blocker/discussions)
- 📖 **Documentation**: This README and inline code documentation
- ⭐ **Show your support**: Give us a star if this library helped you!

---

<div align="center">
Made with ❤️ by <a href="https://github.com/sjdroid">Santhosh J.</a>
</div> 
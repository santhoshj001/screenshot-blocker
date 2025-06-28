# 🛡️ Screenshot Blocker

[![JitPack](https://jitpack.io/v/santhoshj/screenshot-blocker.svg)](https://jitpack.io/#santhoshj/screenshot-blocker)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=21)

A comprehensive and production-ready Android library that prevents screenshots and screen recording using Android's `FLAG_SECURE` feature. Built with Kotlin, zero dependencies, and designed for modern Android applications.

## Installation

Add JitPack to your `settings.gradle.kts` (or root `build.gradle.kts`):
```kotlin
// settings.gradle.kts
dependencyResolutionManagement {
    repositories {
        // ... other repositories
        maven { url = uri("https://jitpack.io") }
    }
}
```

Then, add the library to your app's `build.gradle.kts`:
```kotlin
// app/build.gradle.kts
dependencies {
    implementation("com.github.santhoshj:screenshot-blocker:1.3.0")
}
```

## Quick Start

Initialize in your `Application` class to automatically protect all activities:

```kotlin
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ScreenshotBlocker.init(this) // Enables global screenshot blocking
    }
}
```

For more detailed instructions and advanced usage, please see the [USAGE.md](USAGE.md) file.

## How It Works

For a detailed explanation of the library's architecture and internal workings, please see the [HOW_IT_WORKS.md](HOW_IT_WORKS.md) file.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request. For major changes, please open an issue first to discuss what you would like to change.

## License

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
 
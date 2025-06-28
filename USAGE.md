# Usage Guide

This guide explains how to integrate and use the ScreenshotBlocker library in your Android project.

## 1. Installation

Add the JitPack repository to your root `build.gradle.kts` or `settings.gradle.kts` file:

```kotlin
// settings.gradle.kts
dependencyResolutionManagement {
    repositories {
        // ...
        maven { url 'https://jitpack.io' }
    }
}
```

Then, add the library dependency to your app-level `build.gradle.kts` file:

```kotlin
// app/build.gradle.kts
dependencies {
    implementation('com.github.your-username/screenshot-blocker:1.3.0') // Replace with the actual username and latest version
}
```

## 2. Basic Setup

The easiest way to get started is to initialize the library in your `Application` class. This will automatically enable screenshot blocking for all activities in your app.

```kotlin
import com.sjdroid.screenshotblocker.ScreenshotBlocker

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize the library to block screenshots globally
        ScreenshotBlocker.init(this)
    }
}
```

## 3. Advanced Configuration

You can customize the library's behavior during initialization.

```kotlin
ScreenshotBlocker.init(
    application = this,
    enableGlobally = true,      // Default: true. Set to false to disable global blocking.
    debugMode = BuildConfig.DEBUG, // Default: false. Disables all blocking when true.
    policy = null               // Optional: Provide a custom policy (see below).
)
```

## 4. Manual Per-Activity Control

You can manually enable or disable screenshot blocking for specific activities at any time. This will always override the global setting or any active policy.

```kotlin
class MySecureActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // ...

        // Manually enable blocking for this activity
        ScreenshotBlocker.enableFor(this)
    }
}

class MyInsecureActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // ...

        // Manually disable blocking for this activity
        ScreenshotBlocker.disableFor(this)
    }
}
```

To clear all manual settings and revert to the global/policy configuration, use:

```kotlin
ScreenshotBlocker.clearAllManualSettings()
```

## 5. Using Policies (Strategy Pattern)

For more fine-grained control, you can use a `WindowSecurePolicy`. This is the most flexible way to manage screenshot blocking.

### Using the Annotation Policy

This is the cleanest way to manage security for multiple activities.

**Step 1: Annotate your activities**

```kotlin
import com.sjdroid.screenshotblocker.SecureScreen

@SecureScreen
class MyProfileActivity : AppCompatActivity() { /* ... */ }

@SecureScreen
class MySettingsActivity : AppCompatActivity() { /* ... */ }

class MyHomeActivity : AppCompatActivity() { /* ... */ } // This one will not be blocked
```

**Step 2: Initialize with the `AnnotationBasedSecurePolicy`**

```kotlin
import com.sjdroid.screenshotblocker.AnnotationBasedSecurePolicy

ScreenshotBlocker.init(
    application = this,
    policy = AnnotationBasedSecurePolicy()
)
```

### Using Other Built-in Policies

- **`ConditionalSecurePolicy`**: Blocks a specific list of activities.
  ```kotlin
  val policy = ConditionalSecurePolicy(setOf(
      MyProfileActivity::class.java.name,
      MySettingsActivity::class.java.name
  ))
  ScreenshotBlocker.init(this, policy = policy)
  ```

- **Combining Policies**: You can combine policies using `AndSecurePolicy` or `OrSecurePolicy`.
  ```kotlin
  val annotationPolicy = AnnotationBasedSecurePolicy()
  val conditionalPolicy = ConditionalSecurePolicy(setOf(AnotherActivity::class.java.name))

  // Block activities that have the annotation OR are in the list
  val combinedPolicy = OrSecurePolicy(annotationPolicy, conditionalPolicy)
  ScreenshotBlocker.init(this, policy = combinedPolicy)
  ```

### Creating a Custom Policy

You can create your own policy by implementing the `WindowSecurePolicy` interface.

```kotlin
import com.sjdroid.screenshotblocker.WindowSecurePolicy

// Example: Block screenshots only for premium users
class PremiumUserPolicy(private val isPremiumUser: Boolean) : WindowSecurePolicy {
    override fun shouldApplyFlagSecure(activity: Activity): Boolean {
        // Only apply to a specific activity and if the user is premium
        return activity is MyPremiumFeatureActivity && isPremiumUser
    }
}

// Initialize with your custom policy
val isUserPremium = checkUserPremiumStatus()
ScreenshotBlocker.init(this, policy = PremiumUserPolicy(isUserPremium))
```

## 6. Usage with Dependency Injection

If you are using a DI framework like Hilt or Koin, you can manage the `ScreenshotBlockerManager` yourself for better testability.

**Step 1: Provide the manager in your DI module**

```kotlin
// Example with Hilt
@Module
@InstallIn(SingletonComponent::class)
object SecurityModule {
    @Provides
    @Singleton
    fun provideScreenshotBlockerManager(@ApplicationContext context: Context): ScreenshotBlockerManager {
        return ScreenshotBlockerManager(context as Application)
    }
}
```

**Step 2: Inject and use the manager**

```kotlin
@AndroidEntryPoint
class MyActivity : AppCompatActivity() {

    @Inject
    lateinit var screenshotBlockerManager: ScreenshotBlockerManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Use the injected instance
        screenshotBlockerManager.enableFor(this)
    }
}
```

When managing the instance yourself, make sure to call `screenshotBlockerManager.init()` in your `Application` class.

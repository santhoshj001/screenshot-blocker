# Consumer ProGuard rules for Screenshot Blocker Library
# These rules will be applied to projects that use this library

# Keep the main public API
-keep public class com.sjdroid.screenshotblocker.ScreenshotBlocker {
    public *;
}

# Keep the adapter class as it may be extended by users
-keep public class com.sjdroid.screenshotblocker.ActivityLifecycleCallbacksAdapter {
    public *;
    protected *;
}

# Keep all public methods that may be called by reflection or external code
-keepclassmembers class com.sjdroid.screenshotblocker.** {
    public *;
} 
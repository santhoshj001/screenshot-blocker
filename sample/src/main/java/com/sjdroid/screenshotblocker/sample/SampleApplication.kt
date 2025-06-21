package com.sjdroid.screenshotblocker.sample

import android.app.Application
import com.sjdroid.screenshotblocker.AnnotationBasedSecurePolicy
import com.sjdroid.screenshotblocker.ScreenshotBlocker

/**
 * Sample Application demonstrating the primary use case of the Screenshot Blocker library.
 */
class SampleApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        //
        // 🚀 CORE FUNCTIONALITY: GLOBAL SCREENSHOT BLOCKING
        //
        // This is the simplest and most powerful way to use the library.
        // By calling init() with `enableGlobally = true`, screenshot blocking will be
        // automatically enabled for ALL activities in your application.
        // No per-activity code is required.
        //
        // This is the recommended approach for most applications.
        //
        ScreenshotBlocker.init(
            application = this,
            enableGlobally = true, // ✅ This is the key to global protection!
            debugMode = BuildConfig.DEBUG // Recommended: automatically disables blocking in debug builds
        )
        
        /*
         For advanced, fine-grained control, you can use policies.
         Example: Initialize with global blocking disabled and use a policy instead.
         
         ScreenshotBlocker.init(
             application = this,
             enableGlobally = false,
             policy = AnnotationBasedSecurePolicy() // Only secures activities with @SecureScreen
         )
        */
    }
} 
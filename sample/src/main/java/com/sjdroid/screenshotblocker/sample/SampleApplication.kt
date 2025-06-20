package com.sjdroid.screenshotblocker.sample

import android.app.Application
import com.sjdroid.screenshotblocker.ScreenshotBlocker

/**
 * Sample Application demonstrating Screenshot Blocker initialization
 */
class SampleApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize Screenshot Blocker with global protection
        // This will automatically protect all activities in the app
        ScreenshotBlocker.init(this, enableGlobally = true)
        
        // Alternative: Initialize without global protection
        // ScreenshotBlocker.init(this, enableGlobally = false)
        // Then manually enable for specific activities
    }
} 
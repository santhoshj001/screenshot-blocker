package com.sjdroid.screenshotblocker.sample

import android.app.Application
import com.sjdroid.screenshotblocker.ScreenshotBlocker

/**
 * Sample Application for testing Screenshot Blocker library.
 * 
 * Global blocking is DISABLED so we can test manually with the radio button.
 */
class SampleApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize the library but DON'T enable global blocking
        // so we can test manually with the radio button in MainActivity
        ScreenshotBlocker.init(
            application = this,
            enableGlobally = false // ❌ Disabled for manual testing
        )
    }
} 
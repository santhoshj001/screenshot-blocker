package com.sjdroid.screenshotblocker.sample

import android.app.Application
import com.sjdroid.screenshotblocker.AnnotationBasedSecurePolicy
import com.sjdroid.screenshotblocker.ScreenshotBlocker

/**
 * Sample Application demonstrating Screenshot Blocker initialization with various configurations
 */
class SampleApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize ScreenshotBlocker with comprehensive configuration
        ScreenshotBlocker.init(
            application = this,
            enableGlobally = false,  // Let policies control security instead of global flag
            debugMode = BuildConfig.DEBUG, // Automatically disable in debug builds
            policy = AnnotationBasedSecurePolicy() // Use annotation-based policy
        )
        
        /* Alternative initialization examples:
        
        // Simple global protection:
        ScreenshotBlocker.init(this)
        
        // Global protection with debug override:
        ScreenshotBlocker.init(this, enableGlobally = true, debugMode = true)
        
        // Conditional policy for specific activities:
        ScreenshotBlocker.init(
            this, 
            enableGlobally = false,
            policy = ConditionalSecurePolicy("LoginActivity", "PaymentActivity")
        )
        
        // Always secure all activities:
        ScreenshotBlocker.init(
            this,
            enableGlobally = false,
            policy = AlwaysSecurePolicy()
        )
        
        // Combined policies:
        ScreenshotBlocker.init(
            this,
            enableGlobally = false,
            policy = OrSecurePolicy(
                AnnotationBasedSecurePolicy(),
                ConditionalSecurePolicy("PaymentActivity")
            )
        )
        */
    }
} 
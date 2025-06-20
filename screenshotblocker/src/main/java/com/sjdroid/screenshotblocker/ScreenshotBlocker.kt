package com.sjdroid.screenshotblocker

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.view.WindowManager

/**
 * ScreenshotBlocker - A simple Android library to block screenshots using FLAG_SECURE
 * 
 * Features:
 * - Global screenshot blocking for all activities
 * - Per-activity control
 * - Uses standard Android APIs only
 * - Lightweight and safe
 * 
 * Usage:
 * 1. Initialize in Application.onCreate(): ScreenshotBlocker.init(this)
 * 2. Manual control: ScreenshotBlocker.enableFor(activity) / disableFor(activity)
 * 
 * @author Santhosh J.
 * @version 1.0.0
 */
object ScreenshotBlocker {
    
    private var globalEnable = false
    private var isInitialized = false
    
    /**
     * Initialize the ScreenshotBlocker with global settings
     * 
     * @param application The application instance
     * @param enableGlobally Whether to enable screenshot blocking for all activities automatically
     */
    fun init(application: Application, enableGlobally: Boolean = true) {
        if (isInitialized) {
            return // Already initialized
        }
        
        globalEnable = enableGlobally
        isInitialized = true
        
        application.registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacksAdapter() {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                if (globalEnable) {
                    enableFor(activity)
                }
            }
        })
    }
    
    /**
     * Enable screenshot blocking for a specific activity
     * 
     * @param activity The activity to enable screenshot blocking for
     */
    fun enableFor(activity: Activity) {
        try {
            activity.window.setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
            )
        } catch (e: Exception) {
            // Silently fail if there's an issue (e.g., activity is finishing)
        }
    }
    
    /**
     * Disable screenshot blocking for a specific activity
     * 
     * @param activity The activity to disable screenshot blocking for
     */
    fun disableFor(activity: Activity) {
        try {
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
        } catch (e: Exception) {
            // Silently fail if there's an issue (e.g., activity is finishing)
        }
    }
    
    /**
     * Check if global screenshot blocking is enabled
     * 
     * @return true if global blocking is enabled, false otherwise
     */
    fun isGloballyEnabled(): Boolean = globalEnable
    
    /**
     * Check if the library has been initialized
     * 
     * @return true if initialized, false otherwise
     */
    fun isInitialized(): Boolean = isInitialized
} 
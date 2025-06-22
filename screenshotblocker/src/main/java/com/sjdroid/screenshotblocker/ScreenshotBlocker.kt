package com.sjdroid.screenshotblocker

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.view.WindowManager

/**
 * Simple ScreenshotBlocker - Basic Android library to block screenshots using FLAG_SECURE
 * 
 * Usage:
 * 1. Initialize in Application.onCreate(): ScreenshotBlocker.init(this)
 * 2. Manual control: ScreenshotBlocker.enableFor(activity) / disableFor(activity)
 * 
 * @author Santhosh J.
 * @version 1.1.3-simple
 */
object ScreenshotBlocker {
    
    private var globalEnable = false
    private var initialized = false
    private val secureActivities = mutableSetOf<String>()
    
    /**
     * Initialize the ScreenshotBlocker
     * 
     * @param application The application instance
     * @param enableGlobally Whether to enable screenshot blocking for all activities automatically
     */
    @JvmStatic
    @JvmOverloads
    fun init(application: Application, enableGlobally: Boolean = true) {
        if (initialized) {
            return // Already initialized
        }
        
        globalEnable = enableGlobally
        initialized = true
        
        application.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                if (globalEnable) {
                    enableFor(activity)
                }
            }
            
            override fun onActivityResumed(activity: Activity) {
                // Re-apply security when activity resumes
                val activityKey = activity.javaClass.name + "@" + activity.hashCode()
                if (secureActivities.contains(activityKey) || globalEnable) {
                    applySecureFlag(activity, true)
                }
            }
            
            override fun onActivityDestroyed(activity: Activity) {
                // Clean up tracking
                val activityKey = activity.javaClass.name + "@" + activity.hashCode()
                secureActivities.remove(activityKey)
            }
            
            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
        })
    }
    
    /**
     * Enable screenshot blocking for a specific activity
     * 
     * @param activity The activity to enable screenshot blocking for
     */
    @JvmStatic
    fun enableFor(activity: Activity) {
        val activityKey = activity.javaClass.name + "@" + activity.hashCode()
        secureActivities.add(activityKey)
        applySecureFlag(activity, true)
    }
    
    /**
     * Disable screenshot blocking for a specific activity
     * 
     * @param activity The activity to disable screenshot blocking for
     */
    @JvmStatic
    fun disableFor(activity: Activity) {
        val activityKey = activity.javaClass.name + "@" + activity.hashCode()
        secureActivities.remove(activityKey)
        applySecureFlag(activity, false)
    }
    
    /**
     * Check if screenshot blocking is enabled for a specific activity
     * 
     * @param activity The activity to check
     * @return true if screenshot blocking is enabled, false otherwise
     */
    @JvmStatic
    fun isSecureEnabled(activity: Activity): Boolean {
        if (!initialized) {
            return false
        }
        
        val activityKey = activity.javaClass.name + "@" + activity.hashCode()
        return secureActivities.contains(activityKey) || globalEnable
    }
    
    /**
     * Check if global screenshot blocking is enabled
     * 
     * @return true if global blocking is enabled, false otherwise
     */
    @JvmStatic
    fun isGloballyEnabled(): Boolean = globalEnable
    
    /**
     * Check if the library has been initialized
     * 
     * @return true if initialized, false otherwise
     */
    @JvmStatic
    fun isInitialized(): Boolean = initialized
    
    /**
     * Set global enable/disable
     * 
     * @param enable true to enable globally, false to disable
     */
    @JvmStatic
    fun setGloballyEnabled(enable: Boolean) {
        globalEnable = enable
    }
    
    /**
     * Internal method to apply or remove the secure flag
     */
    private fun applySecureFlag(activity: Activity, enable: Boolean) {
        try {
            // Check if activity is still valid
            if (activity.isFinishing || activity.isDestroyed) {
                return
            }
            
            val window = activity.window
            if (enable) {
                window.setFlags(
                    WindowManager.LayoutParams.FLAG_SECURE,
                    WindowManager.LayoutParams.FLAG_SECURE
                )
            } else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
            }
        } catch (e: Exception) {
            // Silently ignore errors to prevent crashes
        }
    }
} 
package com.sjdroid.screenshotblocker

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.view.WindowManager

/**
 * ScreenshotBlocker - A comprehensive Android library to block screenshots using FLAG_SECURE
 * 
 * Features:
 * - Global screenshot blocking for all activities
 * - Per-activity control with lifecycle awareness
 * - Debug override for testing
 * - Runtime introspection APIs
 * - Configurable policies for fine-grained control
 * - Uses standard Android APIs only
 * - Lightweight and safe
 * 
 * Usage:
 * 1. Initialize in Application.onCreate(): ScreenshotBlocker.init(this)
 * 2. Manual control: ScreenshotBlocker.enableFor(activity) / disableFor(activity)
 * 3. Runtime check: ScreenshotBlocker.isSecureEnabled(context)
 * 4. Custom policies: ScreenshotBlocker.setPolicy(ConditionalSecurePolicy("LoginActivity"))
 * 
 * @author Santhosh J.
 * @version 1.1.0
 */
object ScreenshotBlocker {
    
    private var globalEnable = false
    private var isInitialized = false
    private var debugOverride = false
    private var currentPolicy: WindowSecurePolicy? = null
    private val secureActivities = mutableSetOf<String>()
    
    /**
     * Initialize the ScreenshotBlocker with global settings
     * 
     * @param application The application instance
     * @param enableGlobally Whether to enable screenshot blocking for all activities automatically
     * @param debugMode Override to disable security in debug builds (default: auto-detect from BuildConfig)
     * @param policy Custom policy for determining when to apply screenshot blocking
     */
    @JvmStatic
    @JvmOverloads
    fun init(
        application: Application, 
        enableGlobally: Boolean = true,
        debugMode: Boolean? = null,
        policy: WindowSecurePolicy? = null
    ) {
        if (isInitialized) {
            return // Already initialized
        }
        
        globalEnable = enableGlobally
        isInitialized = true
        currentPolicy = policy
        
        // Auto-detect debug mode if not specified
        debugOverride = debugMode ?: try {
            val buildConfigClass = Class.forName("${application.packageName}.BuildConfig")
            val debugField = buildConfigClass.getField("DEBUG")
            debugField.getBoolean(null)
        } catch (e: Exception) {
            false // Default to production mode if BuildConfig not accessible
        }
        
        application.registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacksAdapter() {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                if (shouldSecureActivity(activity)) {
                    enableFor(activity)
                }
            }
            
            override fun onActivityResumed(activity: Activity) {
                // Re-apply security when activity resumes (lifecycle-aware)
                val activityKey = activity.javaClass.name + "@" + activity.hashCode()
                if (secureActivities.contains(activityKey) || shouldSecureActivity(activity)) {
                    applySecureFlag(activity, true)
                }
            }
            
            override fun onActivityDestroyed(activity: Activity) {
                // Clean up tracking
                val activityKey = activity.javaClass.name + "@" + activity.hashCode()
                secureActivities.remove(activityKey)
            }
        })
    }
    
    /**
     * Set a custom policy for determining when screenshot blocking should be applied
     * 
     * @param policy The policy to use, or null to disable policy-based evaluation
     */
    @JvmStatic
    fun setPolicy(policy: WindowSecurePolicy?) {
        currentPolicy = policy
    }
    
    /**
     * Get the current policy
     * 
     * @return The current policy, or null if no policy is set
     */
    @JvmStatic
    fun getPolicy(): WindowSecurePolicy? = currentPolicy
    
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
     * Check if screenshot blocking is enabled for a specific context
     * 
     * @param context The context to check (Activity recommended)
     * @return true if screenshot blocking is enabled, false otherwise
     */
    @JvmStatic
    fun isSecureEnabled(context: Context): Boolean {
        if (!isInitialized || debugOverride) {
            return false
        }
        
        if (context is Activity) {
            val activityKey = context.javaClass.name + "@" + context.hashCode()
            return secureActivities.contains(activityKey) || shouldSecureActivity(context)
        }
        
        return globalEnable
    }
    
    /**
     * Check if global screenshot blocking is enabled
     * 
     * @return true if global blocking is enabled, false otherwise
     */
    @JvmStatic
    fun isGloballyEnabled(): Boolean = globalEnable && !debugOverride
    
    /**
     * Check if the library has been initialized
     * 
     * @return true if initialized, false otherwise
     */
    @JvmStatic
    fun isInitialized(): Boolean = isInitialized
    
    /**
     * Check if debug mode is active (security disabled)
     * 
     * @return true if debug mode is active, false otherwise
     */
    @JvmStatic
    fun isDebugMode(): Boolean = debugOverride
    
    /**
     * Get the number of currently secured activities
     * 
     * @return count of secured activities
     */
    @JvmStatic
    fun getSecuredActivitiesCount(): Int = secureActivities.size
    
    /**
     * Internal method to determine if an activity should be secured based on current policy
     */
    private fun shouldSecureActivity(activity: Activity): Boolean {
        return when {
            debugOverride -> false
            currentPolicy != null -> currentPolicy!!.shouldSecure(activity)
            else -> globalEnable
        }
    }
    
    /**
     * Internal method to apply or remove the secure flag
     */
    private fun applySecureFlag(activity: Activity, enable: Boolean) {
        if (debugOverride) {
            // Skip applying security in debug mode
            return
        }
        
        try {
            if (enable) {
                activity.window.setFlags(
                    WindowManager.LayoutParams.FLAG_SECURE,
                    WindowManager.LayoutParams.FLAG_SECURE
                )
            } else {
                activity.window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
            }
        } catch (e: Exception) {
            // Silently fail if there's an issue (e.g., activity is finishing)
        }
    }
} 
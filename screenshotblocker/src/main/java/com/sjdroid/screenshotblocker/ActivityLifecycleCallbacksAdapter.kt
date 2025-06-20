package com.sjdroid.screenshotblocker

import android.app.Activity
import android.app.Application
import android.os.Bundle

/**
 * ActivityLifecycleCallbacksAdapter - Adapter class that provides empty implementations
 * of all methods in Application.ActivityLifecycleCallbacks.
 * 
 * This allows subclasses to override only the methods they are interested in.
 * This pattern is commonly used in Android APIs to avoid forcing implementations
 * of all interface methods when only a few are needed.
 * 
 * @author Santhosh J.
 * @version 1.0.0
 */
open class ActivityLifecycleCallbacksAdapter : Application.ActivityLifecycleCallbacks {
    
    /**
     * Called when an activity is created
     */
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        // Default empty implementation
    }
    
    /**
     * Called when an activity is started
     */
    override fun onActivityStarted(activity: Activity) {
        // Default empty implementation
    }
    
    /**
     * Called when an activity is resumed
     */
    override fun onActivityResumed(activity: Activity) {
        // Default empty implementation
    }
    
    /**
     * Called when an activity is paused
     */
    override fun onActivityPaused(activity: Activity) {
        // Default empty implementation
    }
    
    /**
     * Called when an activity is stopped
     */
    override fun onActivityStopped(activity: Activity) {
        // Default empty implementation
    }
    
    /**
     * Called when an activity's instance state is being saved
     */
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        // Default empty implementation
    }
    
    /**
     * Called when an activity is destroyed
     */
    override fun onActivityDestroyed(activity: Activity) {
        // Default empty implementation
    }
} 
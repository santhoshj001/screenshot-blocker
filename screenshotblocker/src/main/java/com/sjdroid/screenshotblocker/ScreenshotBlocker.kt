package com.sjdroid.screenshotblocker

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import java.util.WeakHashMap

/**
 * Manages the screenshot blocking functionality. This class contains the core logic
 * and can be instantiated and managed by a Dependency Injection framework for better testability.
 *
 * @param application The application instance.
 * @author Santhosh J.
 * @version 1.3.0
 */
class ScreenshotBlockerManager(private val application: Application) {

    private val manualOverrides = WeakHashMap<Activity, Boolean>()
    private val activeActivities = WeakHashMap<Activity, Unit>()
    private var initialized = false
    private var globalEnable = false
    private var debugMode = false
    private var policy: WindowSecurePolicy? = null

    /**
     * Initializes the ScreenshotBlocker functionality.
     */
    fun init(
        enableGlobally: Boolean = true,
        debugMode: Boolean = false,
        policy: WindowSecurePolicy? = null
    ) {
        this.globalEnable = enableGlobally
        this.debugMode = debugMode
        this.policy = policy

        if (!initialized) {
            application.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
                override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                    activeActivities[activity] = Unit
                    applyPolicyOrGlobalFlag(activity)
                }

                override fun onActivityResumed(activity: Activity) {
                    applyPolicyOrGlobalFlag(activity)
                }

                override fun onActivityDestroyed(activity: Activity) {
                    manualOverrides.remove(activity)
                    activeActivities.remove(activity)
                }

                override fun onActivityStarted(activity: Activity) {}
                override fun onActivityPaused(activity: Activity) {}
                override fun onActivityStopped(activity: Activity) {}
                override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            })
            initialized = true
        } else {
            activeActivities.keys.forEach { activity ->
                activity?.let { applyPolicyOrGlobalFlag(it) }
            }
        }
    }

    fun enableFor(activity: Activity) {
        if (debugMode) {
            Log.d(TAG, "Screenshot blocking is disabled in debug mode. Skipping enableFor for ${activity.javaClass.simpleName}")
            return
        }
        manualOverrides[activity] = true
        applySecureFlag(activity, true)
    }

    fun disableFor(activity: Activity) {
        manualOverrides[activity] = false
        applySecureFlag(activity, false)
    }

    fun clearAllManualSettings() {
        manualOverrides.clear()
        activeActivities.keys.forEach { activity ->
            activity?.let { applyPolicyOrGlobalFlag(it) }
        }
    }

    fun isSecureEnabled(activity: Activity): Boolean {
        if (debugMode) return false
        if (!initialized) return false
        if (manualOverrides.containsKey(activity)) return manualOverrides[activity] == true
        policy?.let { return it.shouldApplyFlagSecure(activity) }
        return globalEnable
    }

    fun isGloballyEnabled(): Boolean = globalEnable

    fun isInitialized(): Boolean = initialized

    fun setGloballyEnabled(enable: Boolean) {
        globalEnable = enable
        activeActivities.keys.forEach { activity ->
            activity?.let { applyPolicyOrGlobalFlag(it) }
        }
    }

    fun isDebugMode(): Boolean = debugMode

    fun getSecuredActivitiesCount(): Int = activeActivities.size

    fun getPolicy(): WindowSecurePolicy? = policy

    fun setPolicy(policy: WindowSecurePolicy?) {
        this.policy = policy
        activeActivities.keys.forEach { activity ->
            activity?.let { applyPolicyOrGlobalFlag(it) }
        }
    }

    @Synchronized
    private fun applyPolicyOrGlobalFlag(activity: Activity) {
        if (debugMode) {
            applySecureFlag(activity, false)
            return
        }

        val shouldBeSecure = when {
            manualOverrides.containsKey(activity) -> manualOverrides[activity] == true
            policy != null -> policy!!.shouldApplyFlagSecure(activity)
            else -> globalEnable
        }
        applySecureFlag(activity, shouldBeSecure)
    }

    private fun applySecureFlag(activity: Activity, enable: Boolean) {
        try {
            if (activity.isFinishing || activity.isDestroyed) {
                Log.d(TAG, "Activity ${activity.javaClass.simpleName} is finishing or destroyed. Skipping FLAG_SECURE operation.")
                return
            }

            val window = activity.window
            if (enable) {
                window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
                Log.d(TAG, "Applied FLAG_SECURE to ${activity.javaClass.simpleName}")
            } else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
                Log.d(TAG, "Cleared FLAG_SECURE from ${activity.javaClass.simpleName}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error applying/clearing FLAG_SECURE for ${activity.javaClass.simpleName}", e)
        }
    }

    companion object {
        private const val TAG = "ScreenshotBlocker"
    }
}

/**
 * Singleton wrapper for [ScreenshotBlockerManager] for easy access.
 * Provides a default instance for simple use cases.
 */
object ScreenshotBlocker {

    private var instance: ScreenshotBlockerManager? = null

    @JvmStatic
    fun getInstance(application: Application): ScreenshotBlockerManager {
        return instance ?: synchronized(this) {
            instance ?: ScreenshotBlockerManager(application).also { instance = it }
        }
    }

    @JvmStatic
    @JvmOverloads
    fun init(
        application: Application,
        enableGlobally: Boolean = true,
        debugMode: Boolean = false,
        policy: WindowSecurePolicy? = null
    ) {
        getInstance(application).init(enableGlobally, debugMode, policy)
    }

    @JvmStatic
    fun enableFor(activity: Activity) = instance?.enableFor(activity)

    @JvmStatic
    fun disableFor(activity: Activity) = instance?.disableFor(activity)

    @JvmStatic
    fun clearAllManualSettings() = instance?.clearAllManualSettings()

    @JvmStatic
    fun isSecureEnabled(activity: Activity): Boolean = instance?.isSecureEnabled(activity) ?: false

    @JvmStatic
    fun isGloballyEnabled(): Boolean = instance?.isGloballyEnabled() ?: false

    @JvmStatic
    fun isInitialized(): Boolean = instance?.isInitialized() ?: false

    @JvmStatic
    fun setGloballyEnabled(enable: Boolean) = instance?.setGloballyEnabled(enable)

    @JvmStatic
    fun isDebugMode(): Boolean = instance?.isDebugMode() ?: false

    @JvmStatic
    fun getSecuredActivitiesCount(): Int = instance?.getSecuredActivitiesCount() ?: 0

    @JvmStatic
    fun getPolicy(): WindowSecurePolicy? = instance?.getPolicy()

    @JvmStatic
    fun setPolicy(policy: WindowSecurePolicy?) = instance?.setPolicy(policy)
} 
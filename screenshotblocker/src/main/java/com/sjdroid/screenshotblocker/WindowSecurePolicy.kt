package com.sjdroid.screenshotblocker

import android.app.Activity
import android.view.WindowManager

/**
 * Interface for defining custom policies to determine when FLAG_SECURE should be applied.
 * Implementations of this interface decide whether a given activity should have screenshot blocking enabled.
 */
interface WindowSecurePolicy {
    /**
     * Determines if the FLAG_SECURE should be applied to the given activity.
     *
     * @param activity The activity to evaluate.
     * @return `true` if FLAG_SECURE should be applied, `false` otherwise.
     */
    fun shouldApplyFlagSecure(activity: Activity): Boolean
}

/**
 * A [WindowSecurePolicy] that always applies [WindowManager.LayoutParams.FLAG_SECURE].
 */
class AlwaysSecurePolicy : WindowSecurePolicy {
    override fun shouldApplyFlagSecure(activity: Activity): Boolean = true
}

/**
 * A [WindowSecurePolicy] that never applies [WindowManager.LayoutParams.FLAG_SECURE].
 * Useful for testing or specific scenarios where blocking is not desired.
 */
class NeverSecurePolicy : WindowSecurePolicy {
    override fun shouldApplyFlagSecure(activity: Activity): Boolean = false
}

/**
 * A [WindowSecurePolicy] that applies [WindowManager.LayoutParams.FLAG_SECURE] based on a list of activity class names.
 * The flag will be applied if the activity's class name is present in the provided list.
 */
class ConditionalSecurePolicy(private val securedActivityClassNames: Set<String>) : WindowSecurePolicy {
    override fun shouldApplyFlagSecure(activity: Activity): Boolean {
        return securedActivityClassNames.contains(activity.javaClass.name)
    }
}

/**
 * A [WindowSecurePolicy] that combines multiple policies with a logical AND operation.
 * All contained policies must return `true` for the flag to be applied.
 */
class AndSecurePolicy(private vararg val policies: WindowSecurePolicy) : WindowSecurePolicy {
    override fun shouldApplyFlagSecure(activity: Activity): Boolean {
        return policies.all { it.shouldApplyFlagSecure(activity) }
    }
}

/**
 * A [WindowSecurePolicy] that combines multiple policies with a logical OR operation.
 * If any contained policy returns `true`, the flag will be applied.
 */
class OrSecurePolicy(private vararg val policies: WindowSecurePolicy) : WindowSecurePolicy {
    override fun shouldApplyFlagSecure(activity: Activity): Boolean {
        return policies.any { it.shouldApplyFlagSecure(activity) }
    }
}

/**
 * An annotation to mark activities that should have screenshot blocking enabled.
 * This annotation is used in conjunction with [AnnotationBasedSecurePolicy].
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class SecureScreen

/**
 * A [WindowSecurePolicy] that applies [WindowManager.LayoutParams.FLAG_SECURE] to activities
 * annotated with the [SecureScreen] annotation.
 */
class AnnotationBasedSecurePolicy : WindowSecurePolicy {
    override fun shouldApplyFlagSecure(activity: Activity): Boolean {
        return activity.javaClass.isAnnotationPresent(SecureScreen::class.java)
    }
}

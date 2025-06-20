package com.sjdroid.screenshotblocker

import android.app.Activity
import android.content.Context

/**
 * Interface for defining custom policies for when screenshot blocking should be applied.
 * 
 * This provides fine-grained control over enforcement of FLAG_SECURE.
 */
interface WindowSecurePolicy {
    /**
     * Determines whether screenshot blocking should be enabled for the given activity.
     * 
     * @param activity The activity to evaluate
     * @return true if screenshot blocking should be enabled, false otherwise
     */
    fun shouldSecure(activity: Activity): Boolean
}

/**
 * Default implementation that always enables screenshot blocking.
 */
class AlwaysSecurePolicy : WindowSecurePolicy {
    override fun shouldSecure(activity: Activity): Boolean = true
}

/**
 * Default implementation that never enables screenshot blocking.
 * Useful for testing or debug scenarios.
 */
class NeverSecurePolicy : WindowSecurePolicy {
    override fun shouldSecure(activity: Activity): Boolean = false
}

/**
 * Conditional policy that enables screenshot blocking based on activity class names.
 * 
 * @param secureActivityClasses Set of activity class names that should be secured
 */
class ConditionalSecurePolicy(
    private val secureActivityClasses: Set<String>
) : WindowSecurePolicy {
    
    constructor(vararg activityClasses: String) : this(activityClasses.toSet())
    
    override fun shouldSecure(activity: Activity): Boolean {
        return secureActivityClasses.contains(activity.javaClass.name) ||
               secureActivityClasses.contains(activity.javaClass.simpleName)
    }
}

/**
 * Policy that enables screenshot blocking based on activity annotations.
 * 
 * Looks for activities annotated with @SecureScreen.
 */
class AnnotationBasedSecurePolicy : WindowSecurePolicy {
    override fun shouldSecure(activity: Activity): Boolean {
        return activity.javaClass.isAnnotationPresent(SecureScreen::class.java)
    }
}

/**
 * Policy that combines multiple policies with AND logic.
 * Screenshot blocking is enabled only if ALL policies return true.
 */
class AndSecurePolicy(private val policies: List<WindowSecurePolicy>) : WindowSecurePolicy {
    constructor(vararg policies: WindowSecurePolicy) : this(policies.toList())
    
    override fun shouldSecure(activity: Activity): Boolean {
        return policies.all { it.shouldSecure(activity) }
    }
}

/**
 * Policy that combines multiple policies with OR logic.
 * Screenshot blocking is enabled if ANY policy returns true.
 */
class OrSecurePolicy(private val policies: List<WindowSecurePolicy>) : WindowSecurePolicy {
    constructor(vararg policies: WindowSecurePolicy) : this(policies.toList())
    
    override fun shouldSecure(activity: Activity): Boolean {
        return policies.any { it.shouldSecure(activity) }
    }
}

/**
 * Annotation to mark activities that should have screenshot blocking enabled.
 * Used by AnnotationBasedSecurePolicy.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class SecureScreen 
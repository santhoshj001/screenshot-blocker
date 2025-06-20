package com.sjdroid.screenshotblocker

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test for ScreenshotBlocker, which will execute on an Android device.
 * 
 * These tests verify that the ScreenshotBlocker library can be properly initialized 
 * and basic functionality works on real Android devices.
 */
@RunWith(AndroidJUnit4::class)
class ScreenshotBlockerInstrumentedTest {

    @Test
    fun testLibraryIntegration() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.sjdroid.screenshotblocker.test", appContext.packageName)
    }

    @Test
    fun testScreenshotBlockerBasicFunctionality() {
        val testApplication: Application = ApplicationProvider.getApplicationContext()
        
        // Initialize the library
        ScreenshotBlocker.init(testApplication, enableGlobally = false)
        
        // Verify initialization
        assertTrue("Should be initialized after init()", ScreenshotBlocker.isInitialized())
        
        // Test basic runtime introspection
        assertFalse("Debug mode should be disabled by default", ScreenshotBlocker.isDebugMode())
        assertTrue("Secured activities count should be >= 0", ScreenshotBlocker.getSecuredActivitiesCount() >= 0)
    }

    @Test
    fun testPolicyFunctionality() {
        val testApplication: Application = ApplicationProvider.getApplicationContext()
        
        // Initialize with a policy
        val policy = AlwaysSecurePolicy()
        ScreenshotBlocker.init(
            testApplication, 
            enableGlobally = false,
            policy = policy
        )
        
        assertTrue("Should be initialized", ScreenshotBlocker.isInitialized())
        
        // Test policy operations
        val currentPolicy = ScreenshotBlocker.getPolicy()
        assertTrue("Policy should be AlwaysSecurePolicy", currentPolicy is AlwaysSecurePolicy)
        
        // Test policy changing
        val newPolicy = NeverSecurePolicy()
        ScreenshotBlocker.setPolicy(newPolicy)
        
        val updatedPolicy = ScreenshotBlocker.getPolicy()
        assertTrue("Policy should be updated to NeverSecurePolicy", updatedPolicy is NeverSecurePolicy)
    }

    @Test
    fun testDebugModeConfiguration() {
        val testApplication: Application = ApplicationProvider.getApplicationContext()
        
        // Test debug mode initialization
        ScreenshotBlocker.init(
            testApplication, 
            enableGlobally = false,
            debugMode = true
        )
        
        assertTrue("Should be initialized", ScreenshotBlocker.isInitialized())
        assertTrue("Debug mode should be enabled", ScreenshotBlocker.isDebugMode())
    }

    @Test
    fun testRuntimeIntrospectionAPIs() {
        val testApplication: Application = ApplicationProvider.getApplicationContext()
        
        // Initialize library
        ScreenshotBlocker.init(testApplication, enableGlobally = true)
        
        // Test all runtime introspection APIs
        assertTrue("Should be initialized", ScreenshotBlocker.isInitialized())
        assertTrue("Global mode should be enabled", ScreenshotBlocker.isGloballyEnabled())
        assertTrue("Secured activities count should be >= 0", ScreenshotBlocker.getSecuredActivitiesCount() >= 0)
        
        // Policy might be null
        val policy = ScreenshotBlocker.getPolicy()
        // Just verify the call doesn't crash (policy can be null)
        assertTrue("Policy getter should work", policy == null || policy is WindowSecurePolicy)
    }
} 
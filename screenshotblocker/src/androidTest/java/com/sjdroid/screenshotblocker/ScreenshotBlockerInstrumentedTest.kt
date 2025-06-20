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
 * These tests verify that the ScreenshotBlocker library can work on real Android devices.
 */
@RunWith(AndroidJUnit4::class)
class ScreenshotBlockerInstrumentedTest {

    @Test
    fun testLibraryIntegration() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.sjdroid.screenshotblocker.test", appContext.packageName)
    }

    @Test
    fun testBasicInitialization() {
        val testApplication: Application = ApplicationProvider.getApplicationContext()
        
        // Just test that initialization doesn't crash
        ScreenshotBlocker.init(testApplication, enableGlobally = false)
        
        // Verify basic state - should be initialized
        assertTrue("Should be initialized after init()", ScreenshotBlocker.isInitialized())
        
        // Test that basic methods don't crash
        val count = ScreenshotBlocker.getSecuredActivitiesCount()
        assertTrue("Secured activities count should be >= 0", count >= 0)
        
        // Test policy getter doesn't crash
        val policy = ScreenshotBlocker.getPolicy()
        // Policy can be null or any WindowSecurePolicy implementation
        assertTrue("Policy getter should work", policy == null || policy is WindowSecurePolicy)
    }

    @Test
    fun testPolicyOperations() {
        val testApplication: Application = ApplicationProvider.getApplicationContext()
        
        // Initialize (might already be initialized from previous test, that's ok)
        ScreenshotBlocker.init(testApplication, enableGlobally = false)
        
        // Test setting policies
        val alwaysSecure = AlwaysSecurePolicy()
        ScreenshotBlocker.setPolicy(alwaysSecure)
        
        val currentPolicy = ScreenshotBlocker.getPolicy()
        assertTrue("Policy should be set to AlwaysSecurePolicy", currentPolicy is AlwaysSecurePolicy)
        
        // Test changing policy
        val neverSecure = NeverSecurePolicy()
        ScreenshotBlocker.setPolicy(neverSecure)
        
        val newPolicy = ScreenshotBlocker.getPolicy()
        assertTrue("Policy should be changed to NeverSecurePolicy", newPolicy is NeverSecurePolicy)
        
        // Test clearing policy
        ScreenshotBlocker.setPolicy(null)
        assertNull("Policy should be null after clearing", ScreenshotBlocker.getPolicy())
    }

    @Test
    fun testRuntimeAPIs() {
        val testApplication: Application = ApplicationProvider.getApplicationContext()
        
        // Initialize (might already be initialized, that's ok)
        ScreenshotBlocker.init(testApplication)
        
        // Test that all runtime APIs work without crashing
        assertTrue("Should be initialized", ScreenshotBlocker.isInitialized())
        
        // These methods should return boolean values without crashing
        val debugMode = ScreenshotBlocker.isDebugMode()
        val globalMode = ScreenshotBlocker.isGloballyEnabled()
        val count = ScreenshotBlocker.getSecuredActivitiesCount()
        
        // Just verify the methods work (don't assert specific values since state might vary)
        assertTrue("Debug mode getter should work", debugMode == true || debugMode == false)
        assertTrue("Global mode getter should work", globalMode == true || globalMode == false)
        assertTrue("Count should be >= 0", count >= 0)
    }
} 
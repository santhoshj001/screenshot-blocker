package com.sjdroid.screenshotblocker

import android.app.Activity
import android.view.WindowManager
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test for ScreenshotBlocker, which will execute on an Android device.
 */
@RunWith(AndroidJUnit4::class)
class ScreenshotBlockerInstrumentedTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(TestActivity::class.java)

    @Test
    fun testLibraryIntegration() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.sjdroid.screenshotblocker.test", appContext.packageName)
    }

    @Test
    fun testScreenshotBlockerEnableDisable() {
        activityRule.scenario.onActivity { activity ->
            // Test enabling screenshot blocking
            ScreenshotBlocker.enableFor(activity)
            
            // Verify FLAG_SECURE is set (we can't directly test the flag, but we can test the method doesn't crash)
            assertTrue("Activity should be running", !activity.isFinishing)
            
            // Test disabling screenshot blocking
            ScreenshotBlocker.disableFor(activity)
            
            // Verify the method executed without error
            assertTrue("Activity should still be running", !activity.isFinishing)
        }
    }

    @Test
    fun testFlagSecureState() {
        activityRule.scenario.onActivity { activity ->
            // Enable screenshot blocking
            ScreenshotBlocker.enableFor(activity)
            
            // Check if FLAG_SECURE is set
            val window = activity.window
            val flags = window.attributes.flags
            val hasSecureFlag = (flags and WindowManager.LayoutParams.FLAG_SECURE) != 0
            
            assertTrue("FLAG_SECURE should be set", hasSecureFlag)
            
            // Disable screenshot blocking
            ScreenshotBlocker.disableFor(activity)
            
            // Check if FLAG_SECURE is cleared
            val flagsAfterDisable = window.attributes.flags
            val hasSecureFlagAfterDisable = (flagsAfterDisable and WindowManager.LayoutParams.FLAG_SECURE) != 0
            
            assertFalse("FLAG_SECURE should be cleared", hasSecureFlagAfterDisable)
        }
    }
}

/**
 * Test activity for instrumented tests
 */
class TestActivity : Activity() {
    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        // Simple test activity
    }
} 
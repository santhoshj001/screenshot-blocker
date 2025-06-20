package com.sjdroid.screenshotblocker

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

/**
 * Unit tests for ScreenshotBlocker
 */
class ScreenshotBlockerTest {

    @Mock
    private lateinit var mockApplication: Application
    
    @Mock
    private lateinit var mockActivity: Activity
    
    @Mock
    private lateinit var mockWindow: Window
    
    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        `when`(mockActivity.window).thenReturn(mockWindow)
        
        // Reset ScreenshotBlocker state before each test
        resetScreenshotBlocker()
    }
    
    @Test
    fun `test initial state`() {
        assertFalse("Should not be initialized initially", ScreenshotBlocker.isInitialized())
        assertFalse("Should not be globally enabled initially", ScreenshotBlocker.isGloballyEnabled())
    }
    
    @Test
    fun `test init with global enable true`() {
        ScreenshotBlocker.init(mockApplication, enableGlobally = true)
        
        assertTrue("Should be initialized after init", ScreenshotBlocker.isInitialized())
        assertTrue("Should be globally enabled", ScreenshotBlocker.isGloballyEnabled())
        
        verify(mockApplication).registerActivityLifecycleCallbacks(any())
    }
    
    @Test
    fun `test init with global enable false`() {
        ScreenshotBlocker.init(mockApplication, enableGlobally = false)
        
        assertTrue("Should be initialized after init", ScreenshotBlocker.isInitialized())
        assertFalse("Should not be globally enabled", ScreenshotBlocker.isGloballyEnabled())
        
        verify(mockApplication).registerActivityLifecycleCallbacks(any())
    }
    
    @Test
    fun `test multiple init calls should not register multiple callbacks`() {
        ScreenshotBlocker.init(mockApplication, enableGlobally = true)
        ScreenshotBlocker.init(mockApplication, enableGlobally = false) // Second call
        
        // Should only register once
        verify(mockApplication, times(1)).registerActivityLifecycleCallbacks(any())
        assertTrue("Should remain globally enabled from first call", ScreenshotBlocker.isGloballyEnabled())
    }
    
    @Test
    fun `test enableFor activity`() {
        ScreenshotBlocker.enableFor(mockActivity)
        
        verify(mockWindow).setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
    }
    
    @Test
    fun `test disableFor activity`() {
        ScreenshotBlocker.disableFor(mockActivity)
        
        verify(mockWindow).clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
    }
    
    @Test
    fun `test enableFor handles exception gracefully`() {
        `when`(mockActivity.window).thenThrow(RuntimeException("Test exception"))
        
        // Should not throw exception
        assertDoesNotThrow {
            ScreenshotBlocker.enableFor(mockActivity)
        }
    }
    
    @Test
    fun `test disableFor handles exception gracefully`() {
        `when`(mockActivity.window).thenThrow(RuntimeException("Test exception"))
        
        // Should not throw exception
        assertDoesNotThrow {
            ScreenshotBlocker.disableFor(mockActivity)
        }
    }
    
    /**
     * Helper method to reset ScreenshotBlocker state using reflection
     * This is needed because ScreenshotBlocker is an object with private state
     */
    private fun resetScreenshotBlocker() {
        try {
            val clazz = ScreenshotBlocker::class.java
            val globalEnableField = clazz.getDeclaredField("globalEnable")
            val isInitializedField = clazz.getDeclaredField("isInitialized")
            
            globalEnableField.isAccessible = true
            isInitializedField.isAccessible = true
            
            globalEnableField.setBoolean(ScreenshotBlocker, false)
            isInitializedField.setBoolean(ScreenshotBlocker, false)
        } catch (e: Exception) {
            // If reflection fails, skip reset (tests may fail but that's acceptable)
        }
    }
    
    private fun assertDoesNotThrow(block: () -> Unit) {
        try {
            block()
        } catch (e: Exception) {
            fail("Expected no exception but got: ${e.message}")
        }
    }
} 
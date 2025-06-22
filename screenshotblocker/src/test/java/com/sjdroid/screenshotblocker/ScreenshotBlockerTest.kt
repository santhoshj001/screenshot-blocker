package com.sjdroid.screenshotblocker

import android.app.Activity
import android.app.Application
import android.view.Window
import android.view.WindowManager
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

/**
 * Simple unit tests for ScreenshotBlocker
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
        `when`(mockActivity.isFinishing).thenReturn(false)
        `when`(mockActivity.isDestroyed).thenReturn(false)
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
    
    @Test
    fun `test basic initialization`() {
        ScreenshotBlocker.init(mockApplication, enableGlobally = false)
        
        assertTrue("Should be initialized after init", ScreenshotBlocker.isInitialized())
        verify(mockApplication).registerActivityLifecycleCallbacks(any())
    }
    
    private fun assertDoesNotThrow(block: () -> Unit) {
        try {
            block()
        } catch (e: Exception) {
            fail("Expected no exception but got: ${e.message}")
        }
    }
} 
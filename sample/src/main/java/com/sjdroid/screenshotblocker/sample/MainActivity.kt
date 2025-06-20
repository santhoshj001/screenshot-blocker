package com.sjdroid.screenshotblocker.sample

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.sjdroid.screenshotblocker.ScreenshotBlocker

/**
 * Main Activity demonstrating Screenshot Blocker usage
 */
class MainActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(createLayout())
        
        // This activity is automatically protected if global blocking is enabled
        // You can also manually control it:
        // ScreenshotBlocker.enableFor(this)  // Enable screenshot blocking
        // ScreenshotBlocker.disableFor(this) // Disable screenshot blocking
    }
    
    private fun createLayout(): android.widget.LinearLayout {
        val layout = android.widget.LinearLayout(this).apply {
            orientation = android.widget.LinearLayout.VERTICAL
            setPadding(64, 64, 64, 64)
        }
        
        // Title
        val title = TextView(this).apply {
            text = "Screenshot Blocker Demo"
            textSize = 24f
            setPadding(0, 0, 0, 32)
        }
        layout.addView(title)
        
        // Status text
        val statusText = TextView(this).apply {
            text = buildString {
                appendLine("Library Status:")
                appendLine("• Initialized: ${ScreenshotBlocker.isInitialized()}")
                appendLine("• Global Protection: ${ScreenshotBlocker.isGloballyEnabled()}")
                appendLine("")
                appendLine("Try taking a screenshot of this screen!")
                appendLine("It should be blocked if protection is enabled.")
            }
            setPadding(0, 0, 0, 32)
        }
        layout.addView(statusText)
        
        // Button to open secure activity
        val secureButton = Button(this).apply {
            text = "Open Secure Activity"
            setOnClickListener {
                startActivity(Intent(this@MainActivity, SecureActivity::class.java))
            }
        }
        layout.addView(secureButton)
        
        // Button to toggle protection for this activity
        val toggleButton = Button(this).apply {
            text = "Disable Protection for This Activity"
            setOnClickListener {
                ScreenshotBlocker.disableFor(this@MainActivity)
                text = "Protection Disabled - Screenshots Allowed"
                isEnabled = false
            }
        }
        layout.addView(toggleButton)
        
        return layout
    }
} 
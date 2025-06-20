package com.sjdroid.screenshotblocker.sample

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.sjdroid.screenshotblocker.ScreenshotBlocker

/**
 * Secure Activity demonstrating manual screenshot blocking
 */
class SecureActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(createLayout())
        
        // Manually enable screenshot blocking for this activity
        // This ensures protection regardless of global settings
        ScreenshotBlocker.enableFor(this)
    }
    
    private fun createLayout(): android.widget.LinearLayout {
        val layout = android.widget.LinearLayout(this).apply {
            orientation = android.widget.LinearLayout.VERTICAL
            setPadding(64, 64, 64, 64)
        }
        
        // Title
        val title = TextView(this).apply {
            text = "🔒 Secure Activity"
            textSize = 24f
            setPadding(0, 0, 0, 32)
        }
        layout.addView(title)
        
        // Secure content text
        val secureText = TextView(this).apply {
            text = buildString {
                appendLine("🛡️ This activity is SECURED")
                appendLine("")
                appendLine("This screen demonstrates:")
                appendLine("• Manual screenshot blocking")
                appendLine("• Protection for sensitive content")
                appendLine("• FLAG_SECURE in action")
                appendLine("")
                appendLine("Try taking a screenshot now!")
                appendLine("📱 Screenshots should be blocked")
                appendLine("🚫 Screen recording should be blocked")
                appendLine("👻 App won't appear in recent apps")
            }
            setPadding(0, 0, 0, 32)
        }
        layout.addView(secureText)
        
        // Sensitive data simulation
        val sensitiveData = TextView(this).apply {
            text = buildString {
                appendLine("💳 SENSITIVE DATA:")
                appendLine("Credit Card: **** **** **** 1234")
                appendLine("PIN: ****")
                appendLine("Balance: $10,000.00")
                appendLine("SSN: ***-**-1234")
            }
            setBackgroundColor(0xFFFFEBEE.toInt())
            setPadding(16, 16, 16, 16)
            setPadding(0, 0, 0, 32)
        }
        layout.addView(sensitiveData)
        
        // Back button
        val backButton = Button(this).apply {
            text = "← Back to Main"
            setOnClickListener {
                finish()
            }
        }
        layout.addView(backButton)
        
        return layout
    }
} 
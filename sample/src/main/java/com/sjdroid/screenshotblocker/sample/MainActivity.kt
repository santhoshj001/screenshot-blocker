package com.sjdroid.screenshotblocker.sample

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.sjdroid.screenshotblocker.*

/**
 * Main Activity demonstrating various Screenshot Blocker features
 */
class MainActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(createLayout())
    }
    
    private fun createLayout(): LinearLayout {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 32, 32, 32)
        }
        
        // Title
        val title = TextView(this).apply {
            text = "📱 Screenshot Blocker Demo"
            textSize = 24f
            setPadding(0, 0, 0, 24)
        }
        layout.addView(title)
        
        // Library Info
        val infoText = TextView(this).apply {
            text = buildString {
                appendLine("🛡️ Comprehensive Screenshot Protection")
                appendLine("")
                appendLine("This demo showcases:")
                appendLine("• Global vs Per-Activity blocking")
                appendLine("• Policy-based control")
                appendLine("• Jetpack Compose integration")
                appendLine("• Runtime introspection")
                appendLine("• Debug mode support")
                appendLine("• Lifecycle-aware behavior")
            }
            setPadding(0, 0, 0, 24)
        }
        layout.addView(infoText)
        
        // Current Status
        val statusText = TextView(this).apply {
            text = buildString {
                appendLine("📊 Current Status:")
                appendLine("• Initialized: ${ScreenshotBlocker.isInitialized()}")
                appendLine("• Global Protection: ${ScreenshotBlocker.isGloballyEnabled()}")
                appendLine("• Debug Mode: ${ScreenshotBlocker.isDebugMode()}")
                appendLine("• This Screen Protected: ${ScreenshotBlocker.isSecureEnabled(this@MainActivity)}")
                appendLine("• Secured Activities: ${ScreenshotBlocker.getSecuredActivitiesCount()}")
            }
            setBackgroundColor(0xFFF5F5F5.toInt())
            setPadding(16, 16, 16, 16)
            setPadding(0, 0, 0, 24)
        }
        layout.addView(statusText)
        
        // Secure this activity button
        val secureThisButton = Button(this).apply {
            text = if (ScreenshotBlocker.isSecureEnabled(this@MainActivity)) 
                "🔓 Disable This Screen" else "🔒 Secure This Screen"
            setOnClickListener {
                if (ScreenshotBlocker.isSecureEnabled(this@MainActivity)) {
                    ScreenshotBlocker.disableFor(this@MainActivity)
                } else {
                    ScreenshotBlocker.enableFor(this@MainActivity)
                }
                // Refresh layout to show updated status
                setContentView(createLayout())
            }
        }
        layout.addView(secureThisButton)
        
        // Policy demo buttons
        val alwaysSecureButton = Button(this).apply {
            text = "📋 Set Always Secure Policy"
            setOnClickListener {
                ScreenshotBlocker.setPolicy(AlwaysSecurePolicy())
                showPolicyStatus("AlwaysSecurePolicy set - all activities will be secured")
            }
        }
        layout.addView(alwaysSecureButton)
        
        val conditionalPolicyButton = Button(this).apply {
            text = "📋 Set Conditional Policy"
            setOnClickListener {
                ScreenshotBlocker.setPolicy(
                    ConditionalSecurePolicy("SecureActivity", "PaymentActivity")
                )
                showPolicyStatus("ConditionalSecurePolicy set - only specific activities secured")
            }
        }
        layout.addView(conditionalPolicyButton)
        
        val annotationPolicyButton = Button(this).apply {
            text = "📋 Set Annotation Policy"
            setOnClickListener {
                ScreenshotBlocker.setPolicy(AnnotationBasedSecurePolicy())
                showPolicyStatus("AnnotationBasedSecurePolicy set - @SecureScreen activities secured")
            }
        }
        layout.addView(annotationPolicyButton)
        
        val clearPolicyButton = Button(this).apply {
            text = "🗑️ Clear Policy"
            setOnClickListener {
                ScreenshotBlocker.setPolicy(null)
                showPolicyStatus("Policy cleared - using global settings")
            }
        }
        layout.addView(clearPolicyButton)
        
        // Show current policy
        val policyText = TextView(this).apply {
            val currentPolicy = ScreenshotBlocker.getPolicy()
            text = "Current Policy: ${currentPolicy?.javaClass?.simpleName ?: "None (Global)"}"
            setBackgroundColor(0xFFE8F5E8.toInt())
            setPadding(16, 16, 16, 16)
            setPadding(0, 0, 0, 16)
        }
        layout.addView(policyText)
        
        // Navigate to Secure Activity
        val secureActivityButton = Button(this).apply {
            text = "🔒 Open Secure Activity (@SecureScreen)"
            setOnClickListener {
                startActivity(Intent(this@MainActivity, SecureActivity::class.java))
            }
        }
        layout.addView(secureActivityButton)
        
        return layout
    }
    
    private fun showPolicyStatus(message: String) {
        // Simple feedback - in a real app you might use a Snackbar
        val layout = findViewById<LinearLayout>(android.R.id.content)
        val feedbackText = TextView(this).apply {
            text = "✅ $message"
            setBackgroundColor(0xFFE8F5E8.toInt())
            setPadding(16, 16, 16, 16)
        }
        layout.addView(feedbackText, 0)
        
        // Remove feedback after 3 seconds
        feedbackText.postDelayed({
            layout.removeView(feedbackText)
        }, 3000)
    }
} 
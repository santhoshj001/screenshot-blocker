package com.sjdroid.screenshotblocker.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.activity.compose.setContent
import com.sjdroid.screenshotblocker.ScreenshotBlocker

/**
 * Simple test app for Screenshot Blocker
 * 
 * This sample app has:
 * - A simple radio button to enable/disable screenshot blocking
 * - Status display showing current state
 * - Clear visual feedback
 */
class MainActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SimpleScreenshotBlockerTest()
                }
            }
        }
    }
}

@Composable
fun SimpleScreenshotBlockerTest() {
    val context = LocalContext.current as MainActivity
    var refreshTrigger by remember { mutableIntStateOf(0) }
    
    // Get current status
    val isInitialized = remember(refreshTrigger) { ScreenshotBlocker.isInitialized() }
    val isGloballyEnabled = remember(refreshTrigger) { ScreenshotBlocker.isGloballyEnabled() }
    val isThisScreenSecure = remember(refreshTrigger) { ScreenshotBlocker.isSecureEnabled(context) }
    
    // Local state for radio button
    var localSecureEnabled by remember { mutableStateOf(isThisScreenSecure) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        
        // Title
        Text(
            text = "🛡️ Screenshot Blocker Test",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        
        // Status Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isThisScreenSecure) 
                    MaterialTheme.colorScheme.errorContainer 
                else 
                    MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Current Status",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                
                Text("Library Initialized: ${if (isInitialized) "✅ YES" else "❌ NO"}")
                Spacer(modifier = Modifier.height(4.dp))
                Text("Global Protection: ${if (isGloballyEnabled) "✅ ENABLED" else "❌ DISABLED"}")
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "This Screen Protected: ${if (isThisScreenSecure) "🔒 SECURE" else "🔓 NOT SECURE"}",
                    fontWeight = FontWeight.Bold,
                    color = if (isThisScreenSecure) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                )
            }
        }
        
        // Control Section
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Test Controls",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                // Radio Button for Screenshot Blocking
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    RadioButton(
                        selected = localSecureEnabled,
                        onClick = {
                            localSecureEnabled = !localSecureEnabled
                            if (localSecureEnabled) {
                                ScreenshotBlocker.enableFor(context)
                            } else {
                                ScreenshotBlocker.disableFor(context)
                            }
                            refreshTrigger++
                        }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (localSecureEnabled) "Screenshot Blocking ENABLED" else "Screenshot Blocking DISABLED",
                        fontSize = 16.sp
                    )
                }
                
                // Instructions
                Text(
                    text = "Try taking a screenshot now to test!",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Instructions
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "How to Test:",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text("1. Toggle the radio button above", fontSize = 14.sp)
                Text("2. Try taking a screenshot", fontSize = 14.sp)
                Text("3. When ENABLED: Screenshot should be blocked", fontSize = 14.sp)
                Text("4. When DISABLED: Screenshot should work normally", fontSize = 14.sp)
            }
        }
    }
} 
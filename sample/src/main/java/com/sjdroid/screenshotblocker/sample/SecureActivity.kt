package com.sjdroid.screenshotblocker.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sjdroid.screenshotblocker.SecureScreen
import com.sjdroid.screenshotblocker.ScreenshotBlocker
import com.sjdroid.screenshotblocker.secure

/**
 * Demonstration of annotation-based screenshot blocking with Compose integration
 */
@SecureScreen // This annotation marks the activity for automatic security
class SecureActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            MaterialTheme {
                SecureScreenDemo()
            }
        }
    }
    
    @Composable
    private fun SecureScreenDemo() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            
            Text(
                text = "🔒 Secure Activity Demo",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            
            Text(
                text = "This screen is secured using @SecureScreen annotation",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            // Security Status Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Security Status:",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "• Secure: ${ScreenshotBlocker.isSecureEnabled(this@SecureActivity)}",
                        color = if (ScreenshotBlocker.isSecureEnabled(this@SecureActivity)) 
                            Color.Green else Color.Red
                    )
                    Text(
                        text = "• Debug Mode: ${ScreenshotBlocker.isDebugMode()}",
                        color = if (ScreenshotBlocker.isDebugMode()) 
                            Color.Red else Color.Green
                    )
                    Text(
                        text = "• Global Protection: ${ScreenshotBlocker.isGloballyEnabled()}",
                        color = if (ScreenshotBlocker.isGloballyEnabled()) 
                            Color.Green else Color.Gray
                    )
                    Text(
                        text = "• Secured Activities: ${ScreenshotBlocker.getSecuredActivitiesCount()}",
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
            
            // Demonstrate SecureScreen composable
            com.sjdroid.screenshotblocker.SecureScreen {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "🛡️ SecureScreen Composable",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "This card is wrapped in SecureScreen composable for extra protection. " +
                                    "It automatically enables screenshot blocking when this composable enters " +
                                    "the composition and disables it when it leaves."
                        )
                    }
                }
            }
            
            // Demonstrate secure modifier
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .secure(), // This modifier adds security to just this composable
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "🔐 .secure() Modifier",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "This card uses the .secure() modifier extension. " +
                                "You can apply this to any Composable to enable screenshot blocking."
                    )
                }
            }
            
            // Sensitive Data Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "🚨 SENSITIVE DATA AREA 🚨",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Red
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Text(
                        text = "💳 Credit Card: 4532 1234 5678 9012",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "🔑 PIN: 1234",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "💰 Balance: $10,000.00",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "🆔 SSN: 123-45-6789",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Try taking a screenshot now! 📱🚫",
                        fontSize = 14.sp,
                        color = Color.Red,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            
            // Feature Demo Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "✨ Features Demonstrated:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val features = listOf(
                        "• @SecureScreen annotation",
                        "• SecureScreen() composable",
                        "• Modifier.secure() extension",
                        "• Runtime introspection APIs",
                        "• Lifecycle-aware behavior",
                        "• Debug mode support",
                        "• Jetpack Compose integration"
                    )
                    
                    features.forEach { feature ->
                        Text(
                            text = feature,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = { finish() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("← Back to Main Activity")
            }
        }
    }
} 
package com.sjdroid.screenshotblocker.sample

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.activity.compose.setContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import com.sjdroid.screenshotblocker.*

/**
 * Clean and comprehensive demonstration of Screenshot Blocker features
 * 
 * This sample app demonstrates:
 * - Basic enable/disable functionality
 * - Policy-based control
 * - Runtime introspection
 * - Integration patterns
 * - Best practices
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
                    ScreenshotBlockerDemo()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenshotBlockerDemo() {
    val context = LocalContext.current as MainActivity
    var refreshTrigger by remember { mutableIntStateOf(0) }
    
    // State derived from library
    val isInitialized = remember(refreshTrigger) { ScreenshotBlocker.isInitialized() }
    val isGloballyEnabled = remember(refreshTrigger) { ScreenshotBlocker.isGloballyEnabled() }
    val isDebugMode = remember(refreshTrigger) { ScreenshotBlocker.isDebugMode() }
    val isThisScreenSecure = remember(refreshTrigger) { ScreenshotBlocker.isSecureEnabled(context) }
    val securedActivitiesCount = remember(refreshTrigger) { ScreenshotBlocker.getSecuredActivitiesCount() }
    val currentPolicy = remember(refreshTrigger) { ScreenshotBlocker.getPolicy() }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        
        // Header
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🛡️ Screenshot Blocker",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "Comprehensive Android Library Demo",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
            }
        }
        
        // --> ADD THIS NEW CARD
        GlobalBlockingInfoCard()
        
        // Status Card
        StatusCard(
            isInitialized = isInitialized,
            isGloballyEnabled = isGloballyEnabled,
            isDebugMode = isDebugMode,
            isThisScreenSecure = isThisScreenSecure,
            securedActivitiesCount = securedActivitiesCount,
            currentPolicy = currentPolicy
        )
        
        // Quick Actions
        QuickActionsCard(
            context = context,
            onRefresh = { refreshTrigger++ }
        )
        
        // Policy Management
        PolicyManagementCard(
            onRefresh = { refreshTrigger++ }
        )
        
        // Navigation
        NavigationCard(context = context)
        
        // Information
        InformationCard()
    }
}

@Composable
fun GlobalBlockingInfoCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Shield, contentDescription = "Shield Icon", tint = MaterialTheme.colorScheme.onTertiaryContainer)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Global Blocking Enabled",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "This app has global screenshot protection enabled by default in the `SampleApplication.kt` file. This is the recommended setup for most apps.",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
fun StatusCard(
    isInitialized: Boolean,
    isGloballyEnabled: Boolean,
    isDebugMode: Boolean,
    isThisScreenSecure: Boolean,
    securedActivitiesCount: Int,
    currentPolicy: WindowSecurePolicy?
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isThisScreenSecure) 
                MaterialTheme.colorScheme.errorContainer 
            else 
                MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "📊 Current Status",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            
            StatusRow("Library Initialized", isInitialized)
            StatusRow("Global Protection", isGloballyEnabled)
            StatusRow("Debug Mode", isDebugMode)
            StatusRow("This Screen Protected", isThisScreenSecure)
            StatusRow("Secured Activities", "$securedActivitiesCount")
            StatusRow("Active Policy", currentPolicy?.javaClass?.simpleName ?: "None")
        }
    }
}

@Composable
fun StatusRow(label: String, value: Any) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = when (value) {
                is Boolean -> if (value) "✅ Yes" else "❌ No"
                else -> value.toString()
            },
            fontWeight = FontWeight.Medium,
            color = when (value) {
                true -> MaterialTheme.colorScheme.primary
                false -> MaterialTheme.colorScheme.error
                else -> MaterialTheme.colorScheme.onSurfaceVariant
            }
        )
    }
}

@Composable
fun QuickActionsCard(
    context: MainActivity,
    onRefresh: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "⚡ Quick Actions",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        ScreenshotBlocker.enableFor(context)
                        onRefresh()
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Icon(Icons.Default.Lock, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Secure")
                }
                
                Button(
                    onClick = {
                        ScreenshotBlocker.disableFor(context)
                        onRefresh()
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(Icons.Default.Lock, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Unsecure")
                }
            }
        }
    }
}

@Composable
fun PolicyManagementCard(onRefresh: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "📋 Policy Management",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                PolicyButton(
                    text = "Always Secure",
                    description = "All activities protected",
                    onClick = {
                        ScreenshotBlocker.setPolicy(AlwaysSecurePolicy())
                        onRefresh()
                    }
                )
                
                PolicyButton(
                    text = "Never Secure",
                    description = "No activities protected",
                    onClick = {
                        ScreenshotBlocker.setPolicy(NeverSecurePolicy())
                        onRefresh()
                    }
                )
                
                PolicyButton(
                    text = "Annotation Based",
                    description = "Only @SecureScreen activities",
                    onClick = {
                        ScreenshotBlocker.setPolicy(AnnotationBasedSecurePolicy())
                        onRefresh()
                    }
                )
                
                PolicyButton(
                    text = "Clear Policy",
                    description = "Use global settings",
                    onClick = {
                        ScreenshotBlocker.setPolicy(null)
                        onRefresh()
                    }
                )
            }
        }
    }
}

@Composable
fun PolicyButton(
    text: String,
    description: String,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            Text(
                text = text,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = description,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun NavigationCard(context: MainActivity) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "🧭 Navigation",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            
            Button(
                onClick = {
                    context.startActivity(Intent(context, SecureActivity::class.java))
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
            ) {
                Icon(Icons.Default.Person, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Open @SecureScreen Activity")
            }
        }
    }
}

@Composable
fun InformationCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "ℹ️ About",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Text(
                text = """
                This demo showcases all Screenshot Blocker features:
                
                • 🔒 Manual enable/disable for specific activities
                • 📋 Policy-based automatic control
                • 🔍 Runtime status introspection
                • 🎯 Lifecycle-aware behavior
                • 🐛 Debug mode support
                • 🧩 Jetpack Compose integration
                
                Try taking a screenshot to see the protection in action!
                """.trimIndent(),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 20.sp
            )
        }
    }
} 
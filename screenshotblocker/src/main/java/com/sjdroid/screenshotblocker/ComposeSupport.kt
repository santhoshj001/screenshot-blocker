package com.sjdroid.screenshotblocker

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalContext

/**
 * Jetpack Compose support for Screenshot Blocker
 * 
 * Provides composables and modifiers to enable screenshot blocking
 * in Compose-based applications.
 */

/**
 * A Composable that enables screenshot blocking for the current screen.
 * 
 * This composable automatically enables FLAG_SECURE when it enters the composition
 * and disables it when it leaves the composition.
 * 
 * @param content The composable content to be secured
 * 
 * Usage:
 * ```
 * SecureScreen {
 *     // Your sensitive UI content here
 *     Text("Sensitive information")
 * }
 * ```
 */
@Composable
fun SecureScreen(content: @Composable () -> Unit) {
    val context = LocalContext.current
    
    DisposableEffect(context) {
        val activity = context as? ComponentActivity
        activity?.let { ScreenshotBlocker.enableFor(it) }
        
        onDispose {
            activity?.let { ScreenshotBlocker.disableFor(it) }
        }
    }
    
    content()
}

/**
 * A Modifier that enables screenshot blocking for the current screen.
 * 
 * This modifier automatically enables FLAG_SECURE when the composable
 * is active and disables it when removed from composition.
 * 
 * @return A modifier that enables screenshot blocking
 * 
 * Usage:
 * ```
 * Column(
 *     modifier = Modifier.secure()
 * ) {
 *     // Your sensitive UI content here
 * }
 * ```
 */
fun Modifier.secure(): Modifier = this.then(
    Modifier.composed {
        val context = LocalContext.current
        
        DisposableEffect(context) {
            val activity = context as? ComponentActivity
            activity?.let { ScreenshotBlocker.enableFor(it) }
            
            onDispose {
                activity?.let { ScreenshotBlocker.disableFor(it) }
            }
        }
        
        Modifier
    }
) 
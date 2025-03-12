package org.lamysia.christmasgrapes

import androidx.compose.ui.window.ComposeUIViewController
import org.lamysia.christmasgrapes.ui.configureIOSSystemBars

fun MainViewController() = ComposeUIViewController { 
    // Configure system bars
    configureIOSSystemBars()
    
    App() 
}
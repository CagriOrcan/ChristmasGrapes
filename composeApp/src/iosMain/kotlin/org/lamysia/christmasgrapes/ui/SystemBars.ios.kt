package org.lamysia.christmasgrapes.ui

import platform.UIKit.UIApplication
import platform.UIKit.UIColor
import platform.UIKit.UIStatusBarStyleLightContent
import platform.UIKit.UIView
import platform.UIKit.setStatusBarStyle

fun configureIOSSystemBars() {
    // Configure status bar style
    UIApplication.sharedApplication.setStatusBarStyle(UIStatusBarStyleLightContent, animated = false)
    
    // Get the key window
    val keyWindow = UIApplication.sharedApplication.keyWindow
    
    // Configure the root view controller
    keyWindow?.rootViewController?.let { viewController ->
        // Make status bar use light content (white color)
        viewController.setNeedsStatusBarAppearanceUpdate()
        
        // Create UIColor from hex values
        val topBarColor = UIColor(
            red = 0x06.toDouble() / 255.0,
            green = 0x59.toDouble() / 255.0,
            blue = 0x7c.toDouble() / 255.0,
            alpha = 1.0
        )

        val bottomBarColor = UIColor(
            red = 0xFF.toDouble() / 255.0,
            green = 0xFF.toDouble() / 255.0,
            blue = 0xFF.toDouble() / 255.0,
            alpha = 1.0
        )
        // Create a background view for the bottom safe area
        val bottomView = UIView().apply {
            backgroundColor = bottomBarColor
            translatesAutoresizingMaskIntoConstraints = false
        }
        
        viewController.view.addSubview(bottomView)
        
        // Position the view at the bottom of the screen
        bottomView.bottomAnchor.constraintEqualToAnchor(viewController.view.bottomAnchor).setActive(true)
        bottomView.leadingAnchor.constraintEqualToAnchor(viewController.view.leadingAnchor).setActive(true)
        bottomView.trailingAnchor.constraintEqualToAnchor(viewController.view.trailingAnchor).setActive(true)
        bottomView.topAnchor.constraintEqualToAnchor(viewController.view.safeAreaLayoutGuide.bottomAnchor).setActive(true)
        
        // Set top bar color
        viewController.view.backgroundColor = topBarColor
    }
} 
package com.vikihyp.shared

import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController

internal val currentRootViewController by lazy {
    ComposeUIViewController {
        VikiApp()
    }
}

fun getRootController(): UIViewController = currentRootViewController
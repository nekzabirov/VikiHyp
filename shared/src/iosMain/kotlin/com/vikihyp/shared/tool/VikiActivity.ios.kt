package com.vikihyp.shared.tool

import androidx.compose.runtime.Composable
import com.nekzabirov.firebaseapp.KActivity
import com.nekzabirov.firebaseapp.KContext
import com.vikihyp.shared.currentRootViewController
import platform.Foundation.NSUserDefaults

@Composable
actual fun getActivity(): KActivity = currentRootViewController

actual fun getKContext(): KContext = KContext()
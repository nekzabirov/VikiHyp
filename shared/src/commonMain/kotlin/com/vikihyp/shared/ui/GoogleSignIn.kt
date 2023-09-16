package com.vikihyp.shared.ui

import androidx.compose.runtime.Composable

expect class GoogleSignIn() {
    @Composable
    fun register(onToken: (String, String) -> Unit)

    fun request()
}
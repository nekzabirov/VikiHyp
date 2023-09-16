package com.vikihyp.shared.ui

import androidx.compose.runtime.Composable
import cocoapods.GoogleSignIn.GIDConfiguration
import cocoapods.GoogleSignIn.GIDSignIn
import com.vikihyp.shared.util.VikiConfig
import com.vikihyp.shared.currentRootViewController

actual class GoogleSignIn {
    private var onToken: ((String, String) -> Unit)? = null

    @Composable
    actual fun register(onToken: (String, String) -> Unit) {
        this.onToken = onToken

        GIDSignIn.sharedInstance.configuration = GIDConfiguration(VikiConfig.GOOGLE_CLIENT_ID)
    }

    actual fun request() {
        GIDSignIn.sharedInstance.signInWithPresentingViewController(currentRootViewController) { result, _ ->
            if (result?.user?.idToken != null) {
                onToken?.invoke(result.user.idToken!!.tokenString, result.user.accessToken.tokenString)
            }
        }
    }
}
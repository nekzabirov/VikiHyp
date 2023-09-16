package com.vikihyp.shared.ui

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.nekzabirov.firebaseapp.AuthFail
import com.vikihyp.shared.util.VikiConfig
import kotlin.coroutines.resumeWithException

actual class GoogleSignIn {
    private var startForResult: ManagedActivityResultLauncher<Intent, ActivityResult>? = null

    private var googleSignInClient: GoogleSignInClient? = null

    @Composable
    actual fun register(onToken: (String, String) -> Unit) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(VikiConfig.GOOGLE_CLIENT_ID)
            .requestProfile()
            .build()

        googleSignInClient = GoogleSignIn.getClient(LocalContext.current, gso)

        startForResult = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode != Activity.RESULT_OK) {
                return@rememberLauncherForActivityResult
            }

            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)

            val account = task.getResult(ApiException::class.java)
            val idToken = account.idToken

            onToken(idToken ?: return@rememberLauncherForActivityResult, "")
        }
    }

    actual fun request() {
        val signInIntent = googleSignInClient?.signInIntent ?: return

        startForResult?.launch(signInIntent)
    }
}
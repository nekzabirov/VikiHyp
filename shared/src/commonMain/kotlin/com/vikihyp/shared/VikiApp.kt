package com.vikihyp.shared

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import com.nekzabirov.firebaseapp.initializeFirebase
import com.vikihyp.shared.repository.AuthRepository
import com.vikihyp.shared.repository.AuthRepositoryImp
import com.vikihyp.shared.screen.AuthScreen
import com.vikihyp.shared.tool.getKContext
import com.vikihyp.shared.ui.VikThem
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    singleOf<AuthRepository>(::AuthRepositoryImp)
}

@Composable
fun VikiApp() {
    initializeFirebase(getKContext())

    startKoin {
        modules(appModule)
    }

    val focusManager = LocalFocusManager.current

    VikThem {
        Surface(
            modifier = Modifier.pointerInput(Unit) {
                detectTapGestures { focusManager.clearFocus() }
            },
            color = MaterialTheme.colorScheme.background
        ) { AuthScreen() }
    }
}
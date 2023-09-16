package com.vikihyp.shared.tool

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.nekzabirov.firebaseapp.KActivity
import com.nekzabirov.firebaseapp.KContext

@Composable
actual fun getActivity(): KActivity {
    val context = LocalContext.current

    return context.getActivity()
}

private fun Context.getActivity(): AppCompatActivity = when (this) {
    is AppCompatActivity -> this
    //is ContextWrapper -> baseContext.getActivity()
    else -> throw Exception("No activity found")
}

private lateinit var vikiApplication: Application

fun setApplication(application: Application) {
    vikiApplication = application
}

actual fun getKContext(): KContext {
    return vikiApplication
}
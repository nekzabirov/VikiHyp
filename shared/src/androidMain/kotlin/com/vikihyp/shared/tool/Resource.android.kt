package com.vikihyp.shared.tool

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font

@SuppressLint("DiscouragedApi")
@Composable
actual fun fontResources(font: String): Font {
    val context: Context = LocalContext.current
    val name = font.substringBefore(".")
    val resId: Int =
        context.resources.getIdentifier(name, "font", context.packageName)
    return Font(resId)
}

@SuppressLint("DiscouragedApi")
@Composable
actual fun stringResource(key: String): String {
    val context: Context = LocalContext.current
    val resId: Int =
        context.resources.getIdentifier(key, "string", context.packageName)
    return androidx.compose.ui.res.stringResource(resId)
}

@Composable
public actual fun stringResource(key: String, vararg args: Any): String {
    val str = stringResource(key)

    return str.format(*args)
}
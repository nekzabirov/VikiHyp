package com.vikihyp.shared.tool

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font

@Composable
expect fun fontResources(font: String): Font

@Composable
expect fun stringResource(key: String): String

@Composable
expect fun stringResource(key: String, vararg args: Any): String
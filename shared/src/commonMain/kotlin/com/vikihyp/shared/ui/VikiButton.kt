package com.vikihyp.shared.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

@Composable
fun VikiButton(
    modifier: Modifier = Modifier,
    icon: Painter,
    enable: Boolean = true,
    loading: Boolean = false,
    onClick: () -> Unit
) = FilledIconButton(
    modifier = modifier.size(64.dp),
    enabled = enable,
    onClick = onClick,
    shape = MaterialTheme.shapes.medium
) {
    if (loading)
        DotsPulsing()
    else
        Icon(
            modifier = Modifier.size(24.dp),
            painter = icon,
            contentDescription = null
        )
}

@Composable
fun VikiButton(
    modifier: Modifier = Modifier,
    label: String,
    enable: Boolean = true,
    loading: Boolean = false,
    onClick: () -> Unit
) = Button(
    modifier = modifier,
    enabled = enable,
    shape = MaterialTheme.shapes.medium,
    onClick = {
        if (!loading) onClick()
    },
) {
    if (loading)
        DotsPulsing()
    else
        Text(label)
}

@Composable
fun VikiButtonSecondary(
    modifier: Modifier = Modifier,
    label: String,
    enable: Boolean = true,
    onClick: () -> Unit
) = Button(
    modifier = modifier,
    enabled = enable,
    colors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.primary
    ),
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
    shape = MaterialTheme.shapes.medium,
    onClick = onClick,
) { Text(label) }

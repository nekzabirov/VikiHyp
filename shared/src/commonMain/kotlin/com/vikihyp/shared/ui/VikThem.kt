package com.vikihyp.shared.ui

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.vikihyp.shared.tool.fontResources


@Composable
internal fun VikThem(content: @Composable () -> Unit) = MaterialTheme(
    colorScheme = colorScheme(),
    typography = typography(),
    shapes = Shapes(
        RoundedCornerShape(25),
        RoundedCornerShape(25),
        RoundedCornerShape(25),
        RoundedCornerShape(25),
        RoundedCornerShape(25)
    ),
    content = content
)

internal fun String.hexToColor() = Color(this.removePrefix("#").toLong(16) or 0x00000000FF000000)

private val vikiPrimaryColor = "#FFD700".hexToColor()
//private val vikiPrimaryColor = Color(0, 35, 99)
private val vikiSecondColor = "#212121".hexToColor()
private val exColor = Color(red = 28, green = 27, blue = 31)


private fun colorScheme() = lightColorScheme(
    primary = vikiPrimaryColor,
    onPrimary = vikiSecondColor,

    secondary = vikiPrimaryColor,
    onSecondary = exColor,

    surface = vikiPrimaryColor,
    onSurface = exColor,

    surfaceVariant = Color.Gray,
    onSurfaceVariant = Color.LightGray,

    background = Color.White
)

@Composable
private fun typography() = Typography(
    displayLarge = VikiTextStyle(16),
    displayMedium = VikiTextStyle(14),
    displaySmall = VikiTextStyle(12),

    titleLarge = VikiTextStyle(32),
    titleMedium = VikiTextStyle(24),
    titleSmall = VikiTextStyle(16),

    bodyLarge = VikiTextStyle(14),
    bodyMedium = VikiTextStyle(12),
    bodySmall = VikiTextStyle(8),

    labelLarge = VikiTextStyle(18),
    labelMedium = VikiTextStyle(16),
    labelSmall = VikiTextStyle(14)
)

@Composable
private fun VikiTextStyle(size: Int) = TextStyle(
    fontSize = size.sp,
    fontFamily = FontFamily(
        fonts = arrayOf(
            fontResources("spicy_rice_regular")
        )
    )
)
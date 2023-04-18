package ru.pgk63.core_ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.pgk63.core_common.enums.theme.ThemeCorners
import ru.pgk63.core_common.enums.theme.ThemeFontSize
import ru.pgk63.core_common.enums.theme.ThemeFontStyle
import ru.pgk63.core_common.enums.theme.ThemeStyle


@Composable
fun MainTheme(
    style: ThemeStyle = ThemeStyle.Green,
    textSize: ThemeFontSize = ThemeFontSize.Medium,
    corners: ThemeCorners = ThemeCorners.Rounded,
    fontFamily: ThemeFontStyle = ThemeFontStyle.Default,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = when (darkTheme) {
        true -> {
            when (style) {
                ThemeStyle.Purple -> purpleDarkPalette
                ThemeStyle.Blue -> blueDarkPalette
                ThemeStyle.Orange -> orangeDarkPalette
                ThemeStyle.Red -> redDarkPalette
                ThemeStyle.Green -> greenDarkPalette
                ThemeStyle.Yellow -> yellowDarkPalette
            }
        }
        false -> {
            when (style) {
                ThemeStyle.Purple -> purpleLightPalette
                ThemeStyle.Blue -> blueLightPalette
                ThemeStyle.Orange -> orangeLightPalette
                ThemeStyle.Red -> redLightPalette
                ThemeStyle.Green -> greenLightPalette
                ThemeStyle.Yellow -> yellowLightPalette
            }
        }
    }

    val typography = PgkTypography(
        heading = TextStyle(
            lineHeight = when (textSize) {
                ThemeFontSize.Small -> 28.sp
                ThemeFontSize.Medium -> 32.sp
                ThemeFontSize.Big -> 36.sp
            },
            fontSize = when (textSize) {
                ThemeFontSize.Small -> 20.sp
                ThemeFontSize.Medium -> 24.sp
                ThemeFontSize.Big -> 28.sp
            },
            fontWeight = FontWeight.Bold
        ),
        body = TextStyle(
            lineHeight = when (textSize) {
                ThemeFontSize.Small -> 22.sp
                ThemeFontSize.Medium -> 24.sp
                ThemeFontSize.Big -> 26.sp
            },
            fontSize = when (textSize) {
                ThemeFontSize.Small -> 14.sp
                ThemeFontSize.Medium -> 16.sp
                ThemeFontSize.Big -> 18.sp
            },
            fontWeight = FontWeight.Normal
        ),
        toolbar = TextStyle(
            lineHeight = when (textSize) {
                ThemeFontSize.Small -> 22.sp
                ThemeFontSize.Medium -> 24.sp
                ThemeFontSize.Big -> 26.sp
            },
            fontSize = when (textSize) {
                ThemeFontSize.Small -> 14.sp
                ThemeFontSize.Medium -> 16.sp
                ThemeFontSize.Big -> 18.sp
            },
            fontWeight = FontWeight.Medium
        ),
        caption = TextStyle(
            lineHeight = when (textSize) {
                ThemeFontSize.Small -> 18.sp
                ThemeFontSize.Medium -> 20.sp
                ThemeFontSize.Big -> 22.sp
            },
            fontSize = when (textSize) {
                ThemeFontSize.Small -> 10.sp
                ThemeFontSize.Medium -> 12.sp
                ThemeFontSize.Big -> 14.sp
            }
        )
    )

    val shapes = PgkShape(
        cornersStyle = when (corners) {
            ThemeCorners.Flat -> RoundedCornerShape(0.dp)
            ThemeCorners.Rounded -> RoundedCornerShape(8.dp)
        }
    )

    val font = PgkFontFamily(
        fontFamily = when(fontFamily){
            ThemeFontStyle.Cursive -> FontFamily.Cursive
            ThemeFontStyle.Serif -> FontFamily.Serif
            ThemeFontStyle.Default -> FontFamily.Default
            ThemeFontStyle.Monospace -> FontFamily.Monospace
            ThemeFontStyle.SansSerif -> FontFamily.SansSerif
        }
    )

    CompositionLocalProvider(
        LocalPgkHabitColors provides colors,
        LocalPgkHabitTypography provides typography,
        LocalPgkHabitShape provides shapes,
        LocalPgkFontFamily provides font,
        content = content
    )
}
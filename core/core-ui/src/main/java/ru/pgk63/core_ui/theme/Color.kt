package ru.pgk63.core_ui.theme

import androidx.compose.ui.graphics.Color

val baseLightPalette = PgkColors(
    primaryBackground = Color(0xFFFFFFFF),
    primaryText = Color(0xFF3D454C),
    secondaryBackground = Color(0xFFF3F4F5),
    secondaryText = Color(0xFF7E7C7F),
    tintColor = Color.Magenta,
    controlColor = Color(0xFF7A8A99),
    drawerBackground = Color(0xFFFFFFFF),
    journalColor = Color(0xFF0324A5),
    errorColor = Color(0xFFFF3377),
)

val baseDarkPalette = PgkColors(
    primaryBackground = Color(0xFF0F0F0F),
    primaryText = Color(0xFFE0E0E0),
    secondaryBackground = Color(0xFF141414),
    secondaryText = Color(0xFF7E7C7F),
    tintColor = Color.Magenta,
    controlColor = Color(0xFF7A8A99),
    drawerBackground = Color(0xFF0F0F0F),
    journalColor = Color(0xFF0324A5),
    errorColor = Color(0xFFFF6699)
)

val purpleLightPalette = baseLightPalette.copy(
    tintColor = Color(0xFFAD57D9)
)

val purpleDarkPalette = baseDarkPalette.copy(
    tintColor = Color(0xFFD580FF)
)

val orangeLightPalette = baseLightPalette.copy(
    tintColor = Color(0xFFFF6619)
)

val orangeDarkPalette = baseDarkPalette.copy(
    tintColor = Color(0xFFFF974D)
)

val blueLightPalette = baseLightPalette.copy(
    tintColor = Color(0xFF4D88FF)
)

val blueDarkPalette = baseDarkPalette.copy(
    tintColor = Color(0xFF99BBFF)
)

val redLightPalette = baseLightPalette.copy(
    tintColor = Color(0xFFE63956)
)

val redDarkPalette = baseDarkPalette.copy(
    tintColor = Color(0xFFFF5975)
)

val greenLightPalette = baseLightPalette.copy(
    tintColor = Color(0xFF12B37D)
)

val greenDarkPalette = baseDarkPalette.copy(
    tintColor = Color(0xFF7EE6C3)
)

val yellowLightPalette = baseLightPalette.copy(
    tintColor = Color(0xFFFFFB00)
)

val yellowDarkPalette = baseDarkPalette.copy(
    tintColor = Color(0xFFA09915)
)
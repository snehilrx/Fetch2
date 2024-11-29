package theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import fetch2.composeapp.generated.resources.Res
import fetch2.composeapp.generated.resources.sohne_buch
import fetch2.composeapp.generated.resources.sohne_buchkursiv
import fetch2.composeapp.generated.resources.sohne_dreiviertelfett
import fetch2.composeapp.generated.resources.sohne_dreiviertelfett_kursiv
import fetch2.composeapp.generated.resources.sohne_extrafett
import fetch2.composeapp.generated.resources.sohne_extraleicht
import fetch2.composeapp.generated.resources.sohne_extraleicht_kursiv
import fetch2.composeapp.generated.resources.sohne_fett
import fetch2.composeapp.generated.resources.sohne_fett_kursiv
import fetch2.composeapp.generated.resources.sohne_halbfett
import fetch2.composeapp.generated.resources.sohne_halbfett_kursiv
import fetch2.composeapp.generated.resources.sohne_kraftig
import fetch2.composeapp.generated.resources.sohne_kraftig_kursiv
import fetch2.composeapp.generated.resources.sohne_leicht
import fetch2.composeapp.generated.resources.sohne_leicht_kursiv
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.Font


object Colors {
    val md_theme_light_primary = Color(0xFF785900)
    val md_theme_light_onPrimary = Color(0xFFFFFFFF)
    val md_theme_light_primaryContainer = Color(0xFFFFDF9E)
    val md_theme_light_onPrimaryContainer = Color(0xFF261A00)
    val md_theme_light_secondary = Color(0xFF785A00)
    val md_theme_light_onSecondary = Color(0xFFFFFFFF)
    val md_theme_light_secondaryContainer = Color(0xFFFFDF9D)
    val md_theme_light_onSecondaryContainer = Color(0xFF251A00)
    val md_theme_light_tertiary = Color(0xFF785A00)
    val md_theme_light_onTertiary = Color(0xFFFFFFFF)
    val md_theme_light_tertiaryContainer = Color(0xFFFFDF9D)
    val md_theme_light_onTertiaryContainer = Color(0xFF251A00)
    val md_theme_light_error = Color(0xFFBA1A1A)
    val md_theme_light_errorContainer = Color(0xFFFFDAD6)
    val md_theme_light_onError = Color(0xFFFFFFFF)
    val md_theme_light_onErrorContainer = Color(0xFF410002)
    val md_theme_light_background = Color(0xFFFFFBFF)
    val md_theme_light_onBackground = Color(0xFF1E1B16)
    val md_theme_light_surface = Color(0xFFFFFBFF)
    val md_theme_light_onSurface = Color(0xFF1E1B16)
    val md_theme_light_surfaceVariant = Color(0xFFEDE1CF)
    val md_theme_light_onSurfaceVariant = Color(0xFF4D4639)
    val md_theme_light_outline = Color(0xFF7F7667)
    val md_theme_light_inverseOnSurface = Color(0xFFF7EFE7)
    val md_theme_light_inverseSurface = Color(0xFF33302A)
    val md_theme_light_inversePrimary = Color(0xFFFABD00)

    @Suppress("unused")
    val md_theme_light_shadow = Color(0xFF000000)
    val md_theme_light_surfaceTint = Color(0xFF785900)
    val md_theme_light_outlineVariant = Color(0xFFD0C5B4)
    val md_theme_light_scrim = Color(0xFF000000)

    val md_theme_dark_primary = Color(0xFF3F2E00)
    val md_theme_dark_onPrimary = Color(0xFFFABD00)
    val md_theme_dark_primaryContainer = Color(0xFF5B4300)
    val md_theme_dark_onPrimaryContainer = Color(0xFFFFDF9E)
    val md_theme_dark_secondary = Color(0xFFF1BF48)
    val md_theme_dark_onSecondary = Color(0xFF3F2E00)
    val md_theme_dark_secondaryContainer = Color(0xFF5B4300)
    val md_theme_dark_onSecondaryContainer = Color(0xFFFFDF9D)
    val md_theme_dark_tertiary = Color(0xFFF1BF48)
    val md_theme_dark_onTertiary = Color(0xFF3F2E00)
    val md_theme_dark_tertiaryContainer = Color(0xFF5B4300)
    val md_theme_dark_onTertiaryContainer = Color(0xFFFFDF9D)
    val md_theme_dark_error = Color(0xFFFFB4AB)
    val md_theme_dark_errorContainer = Color(0xFF93000A)
    val md_theme_dark_onError = Color(0xFF690005)
    val md_theme_dark_onErrorContainer = Color(0xFFFFDAD6)
    val md_theme_dark_background = Color(0xFF1E1B16)
    val md_theme_dark_onBackground = Color(0xFFE9E1D8)
    val md_theme_dark_surface = Color(0xFF1E1B16)
    val md_theme_dark_onSurface = Color(0xFFE9E1D8)
    val md_theme_dark_surfaceVariant = Color(0xFF4D4639)
    val md_theme_dark_onSurfaceVariant = Color(0xFFD0C5B4)
    val md_theme_dark_outline = Color(0xFF998F80)
    val md_theme_dark_inverseOnSurface = Color(0xFF1E1B16)
    val md_theme_dark_inverseSurface = Color(0xFFE9E1D8)
    val md_theme_dark_inversePrimary = Color(0xFF785900)

    @Suppress("unused")
    val md_theme_dark_shadow = Color(0xFF000000)
    val md_theme_dark_surfaceTint = Color(0xFF3F2E00)
    val md_theme_dark_outlineVariant = Color(0xFF4D4639)
    val md_theme_dark_scrim = Color(0xFF000000)
}




@OptIn(ExperimentalResourceApi::class)
@Composable
fun KickassAnimeTheme(
    windowSize: WindowSize,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val sohenFont = FontFamily(
        Font(
            weight = FontWeight.W100,
            style = FontStyle.Normal,
            resource = Res.font.sohne_extraleicht
        ),
        Font(
            weight = FontWeight.W100,
            style = FontStyle.Italic,
            resource = Res.font.sohne_extraleicht_kursiv
        ),
        Font(
            weight = FontWeight.W200,
            style = FontStyle.Normal,
            resource = Res.font.sohne_leicht
        ),
        Font(
            weight = FontWeight.W200,
            style = FontStyle.Italic,
            resource = Res.font.sohne_leicht_kursiv
        ),
        Font(
            weight = FontWeight.W300,
            style = FontStyle.Normal,
            resource = Res.font.sohne_buch
        ),
        Font(
            weight = FontWeight.W300,
            style = FontStyle.Italic,
            resource = Res.font.sohne_buchkursiv
        ),
        Font(
            weight = FontWeight.W400,
            style = FontStyle.Normal,
            resource = Res.font.sohne_kraftig
        ),
        Font(
            weight = FontWeight.W400,
            style = FontStyle.Italic,
            resource = Res.font.sohne_kraftig_kursiv
        ),
        Font(
            weight = FontWeight.W600,
            style = FontStyle.Normal,
            resource = Res.font.sohne_halbfett
        ),
        Font(
            weight = FontWeight.W600,
            style = FontStyle.Italic,
            resource = Res.font.sohne_halbfett_kursiv
        ),
        Font(
            weight = FontWeight.W700,
            style = FontStyle.Normal,
            resource = Res.font.sohne_dreiviertelfett
        ),
        Font(
            weight = FontWeight.W700,
            style = FontStyle.Italic,
            resource = Res.font.sohne_dreiviertelfett_kursiv
        ),
        Font(
            weight = FontWeight.W800,
            style = FontStyle.Normal,
            resource = Res.font.sohne_fett
        ),
        Font(
            weight = FontWeight.W800,
            style = FontStyle.Italic,
            resource = Res.font.sohne_fett_kursiv
        ),
        Font(
            weight = FontWeight.W900,
            style = FontStyle.Normal,
            resource = Res.font.sohne_extrafett
        ),
        Font(
            weight = FontWeight.W900,
            style = FontStyle.Italic,
            resource = Res.font.sohne_extrafett
        ),
    )
    val defaultTypography = Typography()

    val sohenTypography = Typography(
        h1 = defaultTypography.h1.copy(fontFamily = sohenFont),
        h2 = defaultTypography.h2.copy(fontFamily = sohenFont),
        h3 = defaultTypography.h3.copy(fontFamily = sohenFont),
        h4 = defaultTypography.h4.copy(fontFamily = sohenFont),
        h5 = defaultTypography.h5.copy(fontFamily = sohenFont),
        h6 = defaultTypography.h6.copy(fontFamily = sohenFont),
        overline = defaultTypography.overline.copy(fontFamily = sohenFont),
        body1 = defaultTypography.body1.copy(fontFamily = sohenFont),
        body2 = defaultTypography.body2.copy(fontFamily = sohenFont),
        button = defaultTypography.button.copy(fontFamily = sohenFont),
        caption = defaultTypography.caption.copy(fontFamily = sohenFont),
        subtitle1 = defaultTypography.subtitle1.copy(fontFamily = sohenFont),
        subtitle2 = defaultTypography.subtitle2.copy(fontFamily = sohenFont)
    )
    PlatformTheme(windowSize, darkTheme, content, sohenTypography)
}




@Composable
fun PlatformTheme(
    windowSize: WindowSize,
    darkTheme: Boolean,
    content: @Composable () -> Unit,
    sohenTypography: Typography,
) {
    val lightColors = lightColors(
        primary = Colors.md_theme_light_primary,
        onPrimary = Colors.md_theme_light_onPrimary,
        secondary = Colors.md_theme_light_secondary,
        onSecondary = Colors.md_theme_light_onSecondary,
        error = Colors.md_theme_light_error,
        onError = Colors.md_theme_light_onError,
        background = Colors.md_theme_light_background,
        onBackground = Colors.md_theme_light_onBackground,
        surface = Colors.md_theme_light_surface,
        onSurface = Colors.md_theme_light_onSurface,
    )


    val darkColors = darkColors(
        primary = Colors.md_theme_dark_primary,
        onPrimary = Colors.md_theme_dark_onPrimary,
        secondary = Colors.md_theme_dark_secondary,
        onSecondary = Colors.md_theme_dark_onSecondary,
        error = Colors.md_theme_dark_error,
        onError = Colors.md_theme_dark_onError,
        background = Colors.md_theme_dark_background,
        onBackground = Colors.md_theme_dark_onBackground,
        surface = Colors.md_theme_dark_surface,
        onSurface = Colors.md_theme_dark_onSurface,
    )
    val colorSchemeColors = when {
        darkTheme -> darkColors
        else -> lightColors
    }
    val spacings = when(windowSize) {
        WindowSize.COMPACT -> compactSpacings
        WindowSize.MEDIUM -> mediumSpacings
        WindowSize.EXPANDED -> expandedSpacings
    }
    val cellSizes = when(windowSize) {
        WindowSize.COMPACT -> compactCellSizes
        WindowSize.MEDIUM -> mediumCellSizes
        WindowSize.EXPANDED -> expandedCellSizes
    }
    CompositionLocalProvider(
        LocalWindowSize provides windowSize,
        LocalSpacings provides spacings,
        LocalCellSizes provides cellSizes,
    ) {
        MaterialTheme(
            colors = colorSchemeColors,
            content = content,
            typography = sohenTypography
        )
    }
}

val LocalWindowSize = compositionLocalOf { WindowSize.COMPACT }

val LocalSpacings = staticCompositionLocalOf { compactSpacings }

val LocalCellSizes = staticCompositionLocalOf { compactCellSizes }

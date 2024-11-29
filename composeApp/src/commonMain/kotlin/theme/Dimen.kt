package theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

class CellSizes(
    val xxxxs: Dp,
    val xxxs: Dp,
    val xxs: Dp,
    val xs: Dp,
    val sm: Dp,
    val m: Dp,
    val l: Dp,
    val xl: Dp,
    val xxl: Dp,
    val xxxl: Dp,
    val xxxxl: Dp,
)

class Spacings(
    val xxxxs: Dp,
    val xxxs: Dp,
    val xxs: Dp,
    val xs: Dp,
    val sm: Dp,
    val m: Dp,
    val l: Dp,
    val xl: Dp,
    val xxl: Dp,
    val xxxl: Dp,
    val xxxxl: Dp,
)

class FontSizes(
    val h1: TextUnit,
    val h2: TextUnit,
    val h3: TextUnit,
    val h4: TextUnit,
    val h5: TextUnit,
    val h6: TextUnit,
    val overline: TextUnit,
    val body1: TextUnit,
    val body2: TextUnit,
    val button: TextUnit,
    val caption: TextUnit,
    val subtitle1: TextUnit,
    val subtitle2: TextUnit,
)

val compactSpacings = Spacings(
    xxxxs = 1.dp,
    xxxs = 2.dp,
    xxs = 4.dp,
    xs = 6.dp,
    sm = 12.dp,
    m = 16.dp,
    l = 18.dp,
    xl = 24.dp,
    xxl = 32.dp,
    xxxl = 36.dp,
    xxxxl = 48.dp
)

val mediumSpacings = Spacings(
    xxxxs = 2.dp,
    xxxs = 4.dp,
    xxs = 8.dp,
    xs = 14.dp,
    sm = 16.dp,
    m = 18.dp,
    l = 22.dp,
    xl = 28.dp,
    xxl = 36.dp,
    xxxl = 48.dp,
    xxxxl = 56.dp
)

val expandedSpacings = Spacings(
    xxxxs = 4.dp,
    xxxs = 8.dp,
    xxs = 12.dp,
    xs = 16.dp,
    sm = 20.dp,
    m = 24.dp,
    l = 32.dp,
    xl = 40.dp,
    xxl = 48.dp,
    xxxl = 56.dp,
    xxxxl = 64.dp
)

val compactCellSizes = CellSizes(
    xxxxs = 16.dp,
    xxxs = 32.dp,
    xxs = 48.dp,
    xs = 64.dp,
    sm = 72.dp,
    m = 96.dp,
    l = 112.dp,
    xl = 144.dp,
    xxl = 196.dp,
    xxxl = 225.dp,
    xxxxl = 250.dp
)

val mediumCellSizes = CellSizes(
    xxxxs = 20.dp,
    xxxs = 36.dp,
    xxs = 52.dp,
    xs = 68.dp,
    sm = 76.dp,
    m = 100.dp,
    l = 118.dp,
    xl = 152.dp,
    xxl = 212.dp,
    xxxl = 236.dp,
    xxxxl = 272.dp
)

val expandedCellSizes = CellSizes(
    xxxxs = 24.dp,
    xxxs = 42.dp,
    xxs = 56.dp,
    xs = 72.dp,
    sm = 84.dp,
    m = 108.dp,
    l = 124.dp,
    xl = 164.dp,
    xxl = 224.dp,
    xxxl = 248.dp,
    xxxxl = 296.dp
)